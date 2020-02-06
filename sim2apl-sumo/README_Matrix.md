# Using Sim2APL-Sumo with Matrix

## Installation instructions for the Matrix

It is recommended that you install Matrix
within a virtual environment created with conda.

### Creating and activating a conda environment

To create a new virtual environment with conda,
have Anaconda/Miniconda setup on your system.
Installation instructions for Anaconda can be found at:
https://conda.io/docs/user-guide/install/index.html
After installation of Anaconda/Miniconda
execute the following commands.

```
$ conda create -n matrixenv -c conda-forge python=3
```

Activate the conda environment using:

```
$ conda activate matrixenv
```

### Install RabbitMQ

Execute the following command to install RabbitMQ
within the anaconda environment.

```
$ conda install -c conda-forge rabbitmq-server
```

### Install The Matrix

Clone the Matrix git repo, cd into it, and install via pip.

```
$ git clone https://github.com/NSSAC/socioneticus-matrix
$ cd socioneticus-matrix
$ pip install .
```

The above should make the matrix command available.
You can check if installation was successful with the following commands.

```
$ matrix --help
```

### Download and Install Sim2APL

Compile and install Sim2APL and Sim2APL-Sumo using instructions in README.


```
$ git clone https://github.com/parantapa/sim2apl
$ cd sim2apl
$ mvn install
```

```
$ git clone https://github.com/parantapa/sim2apl-sumo
$ cd sim2apl-sumo
$ mvn clean compile assembly:single
```

## Testing Matrix: Simple Setup - Two BluePill agent threads on localhost

### Step 1: Prepare the work directory

Open a *new terminal window*, and execute the following commands.

```
$ mkdir ~/matrixsim
$ cd ~/matrixsim
```

This will create a folder called matrixsim in your home directory.
Create a file called rabbitmq.conf in the ~/matrixsim using your
favorite text editor, with the following content.

```
default_user = user
default_pass = user
```

Also in ~/matrixsim, create a file called matrix.yaml
with the following content.

```
rabbitmq_host: localhost
rabbitmq_port: 5672
rabbitmq_username: user
rabbitmq_password: user
event_exchange: events

sim_nodes:
    - node1
controller_port:
    node1: 16001
num_agentprocs:
    node1: 2
num_storeprocs:
    node1: 1

root_seed: 42
num_rounds: 10
```

```
FIXME: port must be 16001, as it is hard coded in the Sim2APL Matrix code
FIXME: root seed is currently ignored by the Sim2APL Sumo client
FIXME: num rounds must match the number of itertions passed to sumo client
```

### Step 2: Start RabbitMQ

Open a *new terminal window* and execute the following commands:

```
$ source activate matrixenv
$ matrix rabbitmq start -c ~/matrixsim/rabbitmq.conf -r ~/matrixsim -h localhost
```

### Step 3: Start the controller

Open a *new terminal window* and execute the following commands:

```
$ source activate matrixenv
$ matrix controller -c ~/matrixsim/matrix.yaml -n node1
```

### Step 4: Start the BluePill agents and store

Open a *new terminal window* and execute the following commands:

```
$ java -jar target/sim2apl-SUMO-simulation-1.0-SNAPSHOT-jar-with-dependencies.jar \
    -s sumo-gui \
    -c src/main/resources/eichstaedt/eichstaedt.sumo.cfg \
    --step-length 1 \
    --number-of-cars 20 \
    --car-id-prefix node1 \
    --use-matrix true \
    -i 10
```

### Step 5: Cleanup

Wait for the simulation to finish.
All processes except for the RabbitMQ server should exit gracefully.
To stop the RabbitMQ process hit Ctrl-C on the terminal
running RabbitMQ, and wait for it to shutdown cleanly.
