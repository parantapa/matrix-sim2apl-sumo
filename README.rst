A SUMO Simulation Driven by Sim2APL Agents and the Matrix
=========================================================

Dependencies
------------

Executing the simulation requires Java, Maven, Zstd, and SUMO.
Use your Linux distribution's package manager to install them.
The current version of the simulation was tested with
OpenJDK (ver 13.0.1), Maven (ver 3.6.3), and SUMO (ver 1.4.0)

It is recommended that you install Matrix
within a virtual environment created with conda.
To create a new virtual environment with conda,
have Anaconda/Miniconda setup on your system.
Installation instructions for Miniconda can be found at:
``https://docs.conda.io/en/latest/miniconda.html``

Download and build
------------------

Clone the simulation package repository.

.. code:: bash

    $ cd $HOME
    $ git clone matrix-sim2apl-sumo

Create a conda environment for Matrix,
and install Matrix inside it.

.. code:: bash

    $ conda create -n matrix -c conda-forge python=3 rabbitmq-server
    $ conda activate matrix
    $ pip install -e ~/matrix-sim2apl-sumo/matrix

Install ``traas``:

.. code:: bash

    $ cd ~/matrix-sim2apl-sumo/traas
    $ mvn install

Install ``sim2apl``:

.. code:: bash

    $ cd ~/matrix-sim2apl-sumo/sim2apl
    $ mvn install

Compile ``sim2apl-sumo``:

.. code:: bash

    $ cd ~/matrix-sim2apl-sumo/sim2apl-sumo
    $ mvn compile assembly:single

Decompress the Utrecht maps and routes:

.. code:: bash

    $ cd ~/matrix-sim2apl-sumo
    $ unzstd utrecht.passenger.net.xml.zst
    $ unzstd utrecht-unique-routes.xml.zst

Running the simulation
----------------------

The following code runs 10 iterations of the simulation
on a single compute node (the current one)
with 100 cars on the city of Utrecht's map
for 10 iterations.

In a new terminal window execute the following commands to start RabbitMQ:

.. code:: bash

    $ conda activate matrix
    $ cd ~/matrix-sim2apl-sumo
    $ matrix rabbitmq start -c rabbitmq.conf -r . -h localhost

In another new terminal execute the following commands to start the Matrix
controller:

.. code:: bash

    $ conda activate matrix
    $ cd ~/matrix-sim2apl-sumo
    $ matrix controller -c config.yaml -n node0

In a third new terminal execute the following commands to run the simulation
agents:

.. code:: bash

    $ cd ~/matrix-sim2apl-sumo
    $ ./run-sim2apl-sumo.sh
