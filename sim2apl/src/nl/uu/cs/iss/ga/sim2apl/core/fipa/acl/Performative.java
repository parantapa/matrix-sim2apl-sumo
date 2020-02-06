package nl.uu.cs.iss.ga.sim2apl.core.fipa.acl;

/**
 * <p>
 * Taken from FIPA document number: FIPA00037.
 * </p>
 * <p>
 * The definition of a communicative act belonging to the <b>Communicative Act
 * Library</b> (CAL) is normative. That is, if a given agent implements one of
 * the acts in the CAL, then it must implement that act in accordance with the
 * semantic definition in the CAL. However, FIPA- compliant agents are not
 * required to implement any of the CAL languages, except the not-understood
 * composite act.
 * </p>
 * <p>
 * By collecting communicative act definitions in a single, publicly accessible
 * registry, the CAL facilitates the use of standardized communicative acts by
 * agents developed in different contexts. It also provides a greater incentive
 * to developers to make any privately developed communicative acts generally
 * available.
 * </p>
 * <p>
 * The name assigned to a proposed communicative act must uniquely identify
 * which communicative act is used within a FIPA ACL message. It must not
 * conflict with any names currently in the library, and must be an English word
 * or abbreviation that is suggestive of the semantics.
 * </p>
 * <p>
 * FIPA is responsible for maintaining a consistent list of approved and
 * proposed communicative act names and for making this list publicly available
 * to FIPA members and non-members. This list is derived from the FIPA CAL.
 * </p>
 * <p>
 * In addition to the semantic characterization and descriptive information that
 * is required, each communicative act in the CAL may specify additional
 * information, such as stability information, versioning, contact information,
 * different support levels, etc.
 * </p>
 * <p>
 * NB. The ordering in this enum is specifially identical to the one specified
 * in the original <code>ACLMessage</code>
 * </p>
 * 
 * @author Jurian Baas
 * @see ACLMessage
 */
public enum Performative {

	/**
	 * <h1>The action of accepting a previously submitted proposal to perform an
	 * action.</h1>
	 * <p>
	 * <code>accept-proposal</code> is a general-purpose acceptance of a proposal
	 * that was previously submitted (typically through a propose act). The agent
	 * sending the acceptance informs the receiver that it intends that (at some
	 * point in the future) the receiving agent will perform the action, once the
	 * given precondition is, or becomes, true.
	 * </p>
	 * <p>
	 * The proposition given as part of the acceptance indicates the preconditions
	 * that the agent is attaching to the acceptance. A typical use of this is to
	 * finalize the details of a deal in some protocol. For example, a previous
	 * offer to “hold a meeting anytime on Tuesday” might be accepted with an
	 * additional condition that the time of the meeting is 11.00.
	 * </p>
	 * <p>
	 * Note for future extension: an agent may intend that an action become done
	 * without necessarily intending the precondition. For example, during
	 * negotiation about a given task, the negotiating parties may not unequivocally
	 * intend their opening bids: agent a may bid a price p as a precondition, but
	 * be prepared to accept price p'.
	 * </p>
	 */
	ACCEPT_PROPOSAL(0),
	/**
	 * <h1>The action of agreeing to perform some action, possibly in the future.
	 * </h1>
	 * <p>
	 * <code>agree</code> is a general-purpose agreement to a previously submitted
	 * request to perform some action. The agent sending the agreement informs the
	 * receiver that it does intend to perform the action, but not until the given
	 * precondition is true.
	 * </p>
	 * <p>
	 * The proposition given as part of the agree act indicates the qualifiers, if
	 * any, that the agent is attaching to the agreement. This might be used, for
	 * example, to inform the receiver when the agent will execute the action which
	 * it is agreeing to perform.
	 * </p>
	 * <p>
	 * Pragmatic note: The precondition on the action being agreed to can include
	 * the perlocutionary effect of some other CA, such as an inform act. When the
	 * recipient of the agreement (for example, a contract manager) wants the agreed
	 * action to be performed, it should then bring about the precondition by
	 * performing the necessary CA. This mechanism can be used to ensure that the
	 * contractor defers performing the action until the manager is ready for the
	 * action to be done.
	 * </p>
	 */
	AGREE(1),
	/**
	 * <h1>The action of one agent informing another agent that the first agent no
	 * longer has the intention that the second agent perform some action.</h1>
	 * <p>
	 * <code>cancel</code> allows an agent i to inform another agent j that i no
	 * longer intends that j perform a previously requested action. This is not the
	 * same as i informing j that i intends that j not perform the action or stop
	 * performing an action. cancel is simply used to let an agent know that another
	 * agent no longer has a particular intention. (In order for i to stop j from
	 * performing an action, i should request that j stop that action.
	 * </p>
	 * <p>
	 * Of course, nothing in the ACL semantics guarantees that j will actually stop
	 * performing the action; j is free to ignore I’s request.) Finally, note that
	 * the action that is the object of the act of cancellation should be believed
	 * by the sender to be ongoing or to be planned but not yet executed.
	 * </p>
	 */
	CANCEL(2),
	/**
	 * <h1>The action of calling for proposals to perform a given action.</h1>
	 * <p>
	 * <code>cfp</code> is a general-purpose action to initiate a negotiation
	 * process by making a call for proposals to perform the given action. The
	 * actual protocol under which the negotiation process is established is known
	 * either by prior agreement or is explicitly stated in the protocol parameter
	 * of the message.
	 * </p>
	 * <p>
	 * In normal usage, the agent responding to a cfp should answer with a
	 * proposition giving the value of the parameter in the original precondition
	 * expression (see the statement of rational effect for cfp). For example, the
	 * cfp might seek proposals for a journey from Frankfurt to Munich, with a
	 * condition that the mode of travel is by train. A compatible proposal in reply
	 * would be for the 10.45 express train. An incompatible proposal would be to
	 * travel by airplane.
	 * </p>
	 * <p>
	 * Note that cfp can also be used to simply check the availability of an agent
	 * to perform some action. Also note that this formalization of cfp is
	 * restricted to the common case of proposals characterized by a single
	 * parameter (x) in the proposal expression. Other scenarios might involve
	 * multiple proposal parameters, demand curves, free-form responses, and so
	 * forth.
	 * </p>
	 */
	CFP(3),
	/**
	 * <h1>The sender informs the receiver that a given proposition is true, where
	 * the receiver is known to be uncertain about the proposition.</h1>
	 * <p>
	 * <code>confirm</code> indicates that the sending agent:
	 * <ul>
	 * <li>believes that some proposition is true,</li>
	 * <li>intends that the receiving agent also comes to believe that the
	 * proposition is true, and,</li>
	 * <li>believes that the receiver is uncertain of the truth of the
	 * proposition.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The first two properties defined above are straightforward: the sending agent
	 * is sincere4 , and has (somehow) generated the intention that the receiver
	 * should know the proposition (perhaps it has been asked).
	 * </p>
	 * <p>
	 * The last pre-condition determines when the agent should use confirm vs.
	 * inform vs. disconfirm: confirm is used precisely when the other agent is
	 * already known to be uncertain about the proposition (rather than uncertain
	 * about the negation of the proposition).
	 * </p>
	 * <p>
	 * From the receiver's viewpoint, receiving a confirm message entitles it to
	 * believe that:
	 * <ul>
	 * <li>the sender believes the proposition that is the content of the message,
	 * and,</li>
	 * <li>the sender wishes the receiver to believe that proposition also.</li>
	 * </ul>
	 * Whether or not the receiver does, indeed, change its mental attitude to one
	 * of belief in the proposition will be a function of the receiver's trust in
	 * the sincerity and reliability of the sender.
	 * </p>
	 */
	CONFIRM(4),
	/**
	 * <h1>The sender informs the receiver that a given proposition is false, where
	 * the receiver is known to believe, or believe it likely that, the proposition
	 * is true.</h1>
	 * <p>
	 * <code>disconfirm</code> indicates that the sending agent:
	 * <ul>
	 * <li>believes that some proposition is false,</li>
	 * <li>intends that the receiving agent also comes to believe that the
	 * proposition is false, and,</li>
	 * <li>believes that the receiver either believes the proposition, or is
	 * uncertain of the proposition.</li>
	 * </ul>
	 * The first two properties defined above are straightforward: the sending agent
	 * is sincere, and has (somehow) generated the intention that the receiver
	 * should know the proposition (perhaps it has been asked).
	 * </p>
	 * <p>
	 * The last pre-condition determines when the agent should use confirm vs.
	 * inform vs. disconfirm: disconfirm is used precisely when the other agent is
	 * already known to believe the proposition or to be uncertain about it.
	 * </p>
	 * <p>
	 * From the receiver's viewpoint, receiving a disconfirm message entitles it to
	 * believe that:
	 * <ul>
	 * <li>the sender believes that the proposition that is the content of the
	 * message is false, and,</li>
	 * <li>the sender wishes the receiver to believe the negated proposition
	 * also.</li>
	 * </ul>
	 * Whether or not the receiver does, indeed, change its mental attitude to one
	 * of disbelief in the proposition will be a function of the receiver's trust in
	 * the sincerity and reliability of the sender.
	 * </p>
	 */
	DISCONFIRM(5),
	/**
	 * <h1>The action of telling another agent that an action was attempted but the
	 * attempt failed.</h1>
	 * <p>
	 * <code>failure</code> is an abbreviation for informing that an act was
	 * considered feasible by the sender, but was not completed for some given
	 * reason. The agent receiving a failure act is entitled to believe that:
	 * <ul>
	 * <li>the action has not been done, and,</li>
	 * <li>the action is (or, at the time the agent attempted to perform the action,
	 * was) feasible</li>
	 * </ul>
	 * The (causal) reason for the failure is represented by the proposition, which
	 * is the second element of the message content tuple. It may be the constant
	 * true. Often it is the case that there is little either agent can do to
	 * further the attempt to perform the action.
	 * </p>
	 */
	FAILURE(6),
	/**
	 * <h1>The sender informs the receiver that a given proposition is true.</h1>
	 * <p>
	 * <code>inform</code> indicates that the sending agent:
	 * <ul>
	 * <li>holds that some proposition is true,</li>
	 * <li>intends that the receiving agent also comes to believe that the
	 * proposition is true, and,</li>
	 * <li>does not already believe that the receiver has any knowledge of the truth
	 * of the proposition.</li>
	 * </ul>
	 * The first two properties defined above are straightforward: the sending agent
	 * is sincere, and has (somehow) generated the intention that the receiver
	 * should know the proposition (perhaps it has been asked).
	 * </p>
	 * <p>
	 * The last property is concerned with the semantic soundness of the act. If an
	 * agent knows already that some state of the world holds (that the receiver
	 * knows proposition p), it cannot rationally adopt an intention to bring about
	 * that state of the world, that is, that the receiver comes to know p as a
	 * result of the inform act.
	 * </p>
	 * <p>
	 * Note that the property is not as strong as it perhaps appears. The sender is
	 * not required to establish whether the receiver knows p. It is only the case
	 * that, in the case that the sender already happens to know about the state of
	 * the receiver’s beliefs; it should not adopt an intention to tell the receiver
	 * something it already knows. From the receiver’s viewpoint, receiving an
	 * inform message entitles it to believe that:
	 * <ul>
	 * <li>the sender believes the proposition that is the content of the message,
	 * and,</li>
	 * <li>the sender wishes the receiver to believe that proposition also.</li>
	 * </ul>
	 * Whether or not the receiver does, indeed, adopt belief in the proposition
	 * will be a function of the receiver's trust in the sincerity and reliability
	 * of the sender.
	 * </p>
	 */
	INFORM(7),
	/**
	 * <h1>A macro action for the agent of the action to inform the recipient
	 * whether or not a proposition is true.</h1>
	 * <p>
	 * The <code>inform-if</code> macro act is an abbreviation for informing whether
	 * or not a given proposition is believed. The agent which enacts an inform-if
	 * macro-act will actually perform a standard inform act. The content of the
	 * inform act will depend on the informing agent’s beliefs. To inform-if on some
	 * closed proposition φ:
	 * <ul>
	 * <li>if the agent believes the proposition, it will inform the other agent
	 * that φ, and,</li>
	 * <li>if it believes the negation of the proposition, it informs that φ is
	 * false, that is, ¬φ.</li>
	 * </ul>
	 * Under other circumstances, it may not be possible for the agent to perform
	 * this plan. For example, if it has no knowledge of φ, or will not permit the
	 * other party to know (that it believes) φ, it will send a refuse message.
	 * </p>
	 * <p>
	 * Notice that communicative acts can be directly performed, can be planned by
	 * an agent and can be requested of one agent by another. However, macro acts
	 * can be planned and requested, but not directly performed.
	 * </p>
	 */
	INFORM_IF(8),
	/**
	 * <h1>A macro action for sender to inform the receiver the object which
	 * corresponds to a descriptor, for example, a name.</h1>
	 * <p>
	 * The <code>inform-ref</code> macro action allows the sender to inform the
	 * receiver some object that the sender believes corresponds to a descriptor,
	 * such as a name or other identifying description.
	 * </p>
	 * <p>
	 * <code>inform-ref</code> is a macro action, since it corresponds to a
	 * (possibly infinite) disjunction of <code>inform</code> acts, each of which
	 * informs the receiver that “the object corresponding to name is x” for some
	 * given x. For example, an agent can plan an inform-ref of the current time to
	 * agent j, and then perform the act “inform j that the time is 10:45”.
	 * </p>
	 * <p>
	 * The agent performing the act should believe that the object or set of objects
	 * corresponding to the reference expression is the one supplied, and should not
	 * believe that the receiver of the act already knows which object or set of
	 * objects corresponds to the reference expression. The agent may elect to send
	 * a refuse message if it is unable to establish the preconditions of the act.
	 * </p>
	 * <p>
	 * Notice that communicative acts can be directly performed, can be planned by
	 * an agent and can be requested of one agent by another. However, macro acts
	 * can be planned and requested, but not directly performed.
	 * </p>
	 */
	INFORM_REF(9),
	/**
	 * <h1>The sender of the act (for example, i) informs the receiver (for example,
	 * j) that it perceived that j performed some action, but that i did not
	 * understand what j just did. A particular common case is that i tells j that i
	 * did not understand the message that j has just sent to i.</h1>
	 * <p>
	 * The sender of the not-understood communicative act received a communicative
	 * act that it did not understand. There may be several reasons for this: the
	 * agent may not have been designed to process a certain act or class of acts,
	 * or it may have been expecting a different message. For example, it may have
	 * been strictly following a pre-defined protocol, in which the possible message
	 * sequences are predetermined. The not-understood message indicates to that the
	 * sender of the original, that is, misunderstood, action that nothing has been
	 * done as a result of the message. This act may also be used in the general
	 * case for i to inform j that it has not understood j’s action.
	 * </p>
	 * <p>
	 * The second element of the message content tuple is a proposition representing
	 * the reason for the failure to understand. There is no guarantee that the
	 * reason is represented in a way that the receiving agent will understand.
	 * However, a co-operative agent will attempt to explain the misunderstanding
	 * constructively.
	 * </p>
	 * <p>
	 * Note: It is not possible to fully capture the intended semantics of an action
	 * not being understood by another agent. The characterization below captures
	 * that an event happened and that the recipient of the not-understood message
	 * was the agent of that event.
	 * </p>
	 * <p>
	 * φ must be a well formed formula of the content language of the sender agent.
	 * If the sender uses the bare textual message, that is, string in the syntax
	 * definition, as the reason φ, it must be a propositional assertive statement
	 * and (at least) the sender can understand that (natural language) message and
	 * calculate its truth value, that is, decide its assertion is true or false.
	 * So, for example, in the SL language, to use textual message for the
	 * convenience of humans, it must be encapsulated as the constant argument of a
	 * predicate defined in the ontology that the sender uses, for example: (error
	 * "message")
	 * </p>
	 */
	NOT_UNDERSTOOD(10),
	/**
	 * <h1>The action of submitting a proposal to perform a certain action, given
	 * certain preconditions.</h1>
	 * <p>
	 * <code>propose</code> is a general-purpose act to make a proposal or respond
	 * to an existing proposal during a negotiation process by proposing to perform
	 * a given action subject to certain conditions being true. The actual protocol
	 * under which the negotiation process is being conducted is known either by
	 * prior agreement, or is explicitly stated in the protocol parameter of the
	 * message.
	 * </p>
	 * <p>
	 * The proposer (the sender of the propose) informs the receiver that the
	 * proposer will adopt the intention to perform the action once the given
	 * precondition is met, and the receiver notifies the proposer of the receiver’s
	 * intention that the proposer performs the action.
	 * </p>
	 * <p>
	 * A typical use of the condition attached to the proposal is to specify the
	 * price of a bid in an auctioning or negotiation protocol.
	 * </p>
	 */
	PROPOSE(11),
	/**
	 * <h1>The action of asking another agent whether or not a given proposition is
	 * true.</h1>
	 * <p>
	 * <code>query-if</code> is the act of asking another agent whether (it believes
	 * that) a given proposition is true. The sending agent is requesting the
	 * receiver to inform it of the truth of the proposition. The agent performing
	 * the query-if act:
	 * <ul>
	 * <li>has no knowledge of the truth value of the proposition, and,</li>
	 * <li>believes that the other agent can inform the querying agent if it knows
	 * the truth of the proposition.</li>
	 * </ul>
	 * </p>
	 */
	QUERY_IF(12),
	/**
	 * <h1>The action of asking another agent for the object referred to by a
	 * referential expression.</h1>
	 * <p>
	 * <code>query-ref</code> is the act of asking another agent to inform the
	 * requester of the object identified by a descriptor. The sending agent is
	 * requesting the receiver to perform an inform act, containing the object that
	 * corresponds to the descriptor. The agent performing the query-ref act:
	 * <ul>
	 * <li>does not know which object or set of objects corresponds to the
	 * descriptor, and,</li>
	 * <li>believes that the other agent can inform the querying agent the object or
	 * set of objects that correspond to the descriptor.</li>
	 * </ul>
	 * </p>
	 */
	QUERY_REF(13),
	/**
	 * <h1>The action of refusing to perform a given action, and explaining the
	 * reason for the refusal.</h1>
	 * <p>
	 * The refuse act is an abbreviation for denying (strictly speaking, disconfirm)
	 * that an act is possible for the agent to perform and stating the reason why
	 * that is so.
	 * </p>
	 * <p>
	 * The refuse act is performed when the agent cannot meet all of the
	 * preconditions for the action to be carried out, both implicit and explicit.
	 * For example, the agent may not know something it is being asked for, or
	 * another agent requested an action for which it has insufficient privilege.
	 * The agent receiving a refuse act is entitled to believe that:
	 * <ul>
	 * <li>the action has not been done,</li>
	 * <li>the action is not feasible (from the point of view of the sender of the
	 * refusal), and,</li>
	 * <li>the (causal) reason for the refusal is represented by the a proposition
	 * which is the second element of the message content tuple, (which may be the
	 * constant true). There is no guarantee that the reason is represented in a way
	 * that the receiving agent will understand. However, a cooperative agent will
	 * attempt to explain the refusal constructively (see the description of
	 * not-understood).</li>
	 * </ul>
	 * </p>
	 */
	REFUSE(14),
	/**
	 * <h1>The action of rejecting a proposal to perform some action during a
	 * negotiation.</h1>
	 * <p>
	 * <code>reject-proposal</code> is a general-purpose rejection to a previously
	 * submitted proposal. The agent sending the rejection informs the receiver that
	 * it has no intention that the recipient performs the given action under the
	 * given preconditions.
	 * </p>
	 * <p>
	 * The additional proposition represents a reason that the proposal was
	 * rejected. Since it is in general hard to relate cause to effect, the formal
	 * model below only notes that the reason proposition was believed true by the
	 * sender at the time of the rejection. Syntactically the reason should be
	 * treated as a causal explanation for the rejection, even though this is not
	 * established by the formal semantics.
	 * </p>
	 */
	REJECT_PROPOSAL(15),
	/**
	 * <h1>The sender requests the receiver to perform some action. One important
	 * class of uses of the request act is to request the receiver to perform
	 * another communicative act.</h1>
	 * <p>
	 * The sender is requesting the receiver to perform some action. The content of
	 * the message is a description of the action to be performed, in some language
	 * the receiver understands. The action can be any action the receiver is
	 * capable of performing, for example, pick up a box, book a plane flight,
	 * change a password, etc.
	 * </p>
	 * <p>
	 * An important use of the request act is to build composite conversations
	 * between agents, where the actions that are the object of the request act are
	 * themselves communicative acts such as inform.
	 * </p>
	 */
	REQUEST(16),
	/**
	 * <h1>The sender wants the receiver to perform some action when some given
	 * proposition becomes true.</h1>
	 * <p>
	 * <code>request-when</code> allows an agent to inform another agent that a
	 * certain action should be performed as soon as a given precondition, expressed
	 * as a proposition, becomes true.
	 * </p>
	 * <p>
	 * The agent receiving a request-when should either refuse to take on the
	 * commitment, or should arrange to ensure that the action will be performed
	 * when the condition becomes true. This commitment will persist until such time
	 * as it is discharged by the condition becoming true, the requesting agent
	 * cancels the request-when, or the agent decides that it can no longer honour
	 * the commitment, in which case it should send a refuse message to the
	 * originator.
	 * </p>
	 * <p>
	 * No specific commitment is implied by the specification as to how frequently
	 * the proposition is re-evaluated, nor what the lag will be between the
	 * proposition becoming true and the action being enacted. Agents that require
	 * such specific commitments should negotiate their own agreements prior to
	 * submitting the request-when act.
	 * <p>
	 */
	REQUEST_WHEN(17),
	/**
	 * <h1>The sender wants the receiver to perform some action as soon as some
	 * proposition becomes true and thereafter each time the proposition becomes
	 * true again.</h1>
	 * <p>
	 * <code>request-whenever</code> allows an agent to inform another agent that a
	 * certain action should be performed as soon as a given precondition, expressed
	 * as a proposition, becomes true, and that, furthermore, if the proposition
	 * should subsequently become false, the action will be repeated as soon as it
	 * once more becomes true.
	 * </p>
	 * <p>
	 * request-whenever represents a persistent commitment to re-evaluate the given
	 * proposition and take action when its value changes. The originating agent may
	 * subsequently remove this commitment by performing the cancel action.
	 * </p>
	 * <p>
	 * No specific commitment is implied by the specification as to how frequently
	 * the proposition is reevaluated, nor what the lag will be between the
	 * proposition becoming true and the action being enacted. Agents who require
	 * such specific commitments should negotiate their own agreements prior to
	 * submitting the request-when act.
	 * <p>
	 */
	REQUEST_WHENEVER(18),
	/**
	 * <h1>The act of requesting a persistent intention to notify the sender of the
	 * value of a reference, and to notify again whenever the object identified by
	 * the reference changes.</h1>
	 * <p>
	 * The <code>subscribe</code> act is a persistent version of
	 * <code>query-ref</code>, such that the agent receiving the subscribe will
	 * inform the sender of the value of the reference and will continue to send
	 * further informs if the object denoted by the description changes.
	 * </p>
	 * <p>
	 * A subscription set up by a subscribe act is terminated by a cancel act.
	 * </p>
	 */
	SUBSCRIBE(19),
	/**
	 * <h1>The sender wants the receiver to select target agents denoted by a given
	 * description and to send an embedded message to them.</h1>
	 * <p>
	 * The sending agent informs the recipient that the sender wants the receiver to
	 * identify agents that satisfy the given descriptor and to perform the embedded
	 * communicative act to them, that is, the receiver sends the embedded message
	 * to them.
	 * </p>
	 * <p>
	 * On performing the embedded communicative act, the receiver parameter is set
	 * to the denoted agent and the sender is set to the receiver of the proxy
	 * message. If the embedded communicative act contains a reply-to parameter, for
	 * example, in the recruiting case where the protocol parameter is set to
	 * fipa-recruiting, then it should be preserved in the performed message.
	 * </p>
	 * <p>
	 * In the case of a brokering request (that is, the protocol parameter is set to
	 * fipabrokering), the brokerage agent (the receiver of the proxy message) must
	 * record some parameters, for example, conversation-id, reply-with, sender,
	 * etc.) of the received proxy message to forward back the reply message(s) from
	 * the target agents to the corresponding requester agent (the sender of the
	 * proxy message).
	 * </p>
	 */
	PROXY(20),
	/**
	 * <h1>The sender intends that the receiver treat the embedded message as sent
	 * directly to the receiver, and wants the receiver to identify the agents
	 * denoted by the given descriptor and send the received propagate message to
	 * them.</h1>
	 * <p>
	 * This is a compound action of the following two actions:
	 * <ul>
	 * <li>The sending agent requests the recipient to treat the embedded message in
	 * the received propagate message as if it is directly sent from the sender,
	 * that is, as if the sender performed the embedded communicative act directly
	 * to the receiver.</li>
	 * <li>The sender wants the receiver to identify agents denoted by the given
	 * descriptor and to send a modified version of the received propagate message
	 * to them, as described below.</li>
	 * </ul>
	 * On forwarding, the receiver parameter of the forwarded propagate message is
	 * set to the denoted agent(s) and the sender parameter is set to the receiver
	 * of the received propagate message. The sender and receiver of the embedded
	 * communicative act of the forwarded propagate message is also set to the same
	 * agent as the forwarded propagate message’s sender and receiver, respectively.
	 * </p>
	 * <p>
	 * This communicative act is designed for delivering messages through federated
	 * agents by creating a chain (or tree) of propagate messages. An example of
	 * this is instantaneous brokerage requests using a proxy message, or persistent
	 * requests by a requestwhen/request-whenever message embedding a proxy message.
	 * </p>
	 */
	PROPAGATE(21),
	/**
	 * <h1>Message is unknown, not of any FIPA performative</h1>
	 * <p>
	 * Used to catch odd or corrupt messages.
	 * </p>
	 */
	UNKNOWN(-1);
	
	private final int index;
	public final static String[] names = java.util.Arrays.stream(Performative.values()).map(Enum::name).toArray(String[]::new);
	
	public int index() {
		// We cannot use ordinal because of the extra UNKNOWN
		return index;
	}
	
	private Performative(int index) {
		this.index = index;
	}

	/**s
	 * Reconstruct a performative from a given index, usually received over a network.
	 * @param index
	 * @return
	 */
	public static Performative fromIndex(int index) {
		// The non-FIPA performative UNKNOWN seems to be the only exception to the ordinal rule
		// so we check it here
		if(index == UNKNOWN.index) {
			return UNKNOWN;
		}
		// Return the ordinal
		return Performative.values()[index];
	}
	
	public static String[] names() {
		return names;
	}
}
