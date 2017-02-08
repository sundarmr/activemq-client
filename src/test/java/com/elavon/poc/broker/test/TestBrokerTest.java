package com.elavon.poc.broker.test;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class TestBrokerTest extends CamelTestSupport {
	BrokerService broker;
	URI brokerConnectURI;
	org.slf4j.Logger LOG = LoggerFactory.getLogger(TestBrokerTest.class);

	@Before
	public void startBroker() throws Exception {
		broker = new BrokerService();
		broker.setPersistent(false);
		broker.addConnector("vm://localhost");
		broker.start();
		broker.waitUntilStarted();

	}

	@After
	public void stopBroker() throws Exception {
		broker.stop();
		broker.waitUntilStopped();
	}

	@Test
	public void testAProducer() throws Exception {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
		// If the broker has username and password , just a demonstration here
		Connection conn = factory.createQueueConnection("jdoe", "sunflower");
		Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		conn.start();
		// Creating a queue
		Queue queue = sess.createQueue("sample.queue");
		// Creating a producer to send message to activemq
		MessageProducer producer = sess.createProducer(queue);
		// Creating a consumer to receive message to activemq
		MessageConsumer consumer = sess.createConsumer(queue);
		// Send message
		producer.send(sess.createTextMessage("test"));
		// recieve mesasge by waiting for atleast 1000 milliseconds
		Message msg = consumer.receive(1000);
		LOG.info("Message is :" + msg);
		TextMessage msg1 = (TextMessage) msg;
		System.out.println("Message is " + msg);
		// asserting that the mesage is not null
		assertNotNull(msg);
		assertEquals(msg1.getText(), "test");
	}

}