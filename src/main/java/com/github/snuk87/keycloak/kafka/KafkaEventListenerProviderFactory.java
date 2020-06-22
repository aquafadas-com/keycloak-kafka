package com.github.snuk87.keycloak.kafka;

		import org.jboss.logging.Logger;
		import org.keycloak.Config.Scope;
		import org.keycloak.events.EventListenerProvider;
		import org.keycloak.events.EventListenerProviderFactory;
		import org.keycloak.models.KeycloakSession;
		import org.keycloak.models.KeycloakSessionFactory;

public class KafkaEventListenerProviderFactory implements EventListenerProviderFactory {

	private static final Logger LOG = Logger.getLogger(KafkaEventListenerProviderFactory.class);
	private static final String ID = "kafka";

	private KafkaEventListenerProvider instance;

	private String bootstrapServers;
	private String topicEvents;
	private String topicAdminEvents;
	private String clientId;
	private String[] events;

	@Override
	public EventListenerProvider create(KeycloakSession session) {
		if (instance == null) {
			instance = new KafkaEventListenerProvider(bootstrapServers, clientId, topicEvents, events,
					topicAdminEvents);
		}

		return instance;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void init(Scope config) {
		LOG.info("Init kafka module ...");


		topicEvents = System.getenv("KAFKA_TOPIC_EVENTS");
		clientId = System.getenv("KAFKA_CLIENT_ID");
		bootstrapServers = System.getenv("KAFKA_SERVER");
		topicAdminEvents = System.getenv("KAFKA_ADMIN_TOPIC_EVENTS");
		String eventsString = System.getenv("KAFKA_EVENTS");

		if (eventsString != null) {
			events = eventsString.split(",");
		}

		if (topicEvents == null) {
			topicEvents = "keycloak_events";
		}

		if (clientId == null) {
			clientId = "keycloak";
		}

		if (bootstrapServers == null) {
			bootstrapServers = "localhost:9092";
		}

		if (events == null || events.length == 0) {
			events = new String[1];
			events[0] = "REGISTER";
		}
	}

	@Override
	public void postInit(KeycloakSessionFactory arg0) {
		// ignore
	}

	@Override
	public void close() {
		// ignore
	}
}
