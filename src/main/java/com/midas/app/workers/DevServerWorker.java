package com.midas.app.workers;

import com.midas.app.activities.AccountActivityImpl;
import com.midas.app.providers.external.stripe.StripeConfiguration;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.app.workflows.CreateAccountWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import java.io.IOException;
import java.util.Properties;

/** DevServerWorker */
public class DevServerWorker {
  private static String getStripeKey() throws IOException {
    var properties = new Properties();
    var loader = Thread.currentThread().getContextClassLoader();
    var resourceStream = loader.getResourceAsStream("application.properties");
    properties.load(resourceStream);
    return properties.getProperty("stripe.api-key");
  }

  public static void main(String[] args) throws Exception {
    var service = WorkflowServiceStubs.newLocalServiceStubs();
    var client = WorkflowClient.newInstance(service);
    var factory = WorkerFactory.newInstance(client);
    var worker = factory.newWorker(CreateAccountWorkflow.QUEUE_NAME);
    worker.registerWorkflowImplementationTypes(CreateAccountWorkflowImpl.class);

    var stripeConf = new StripeConfiguration();
    stripeConf.setApiKey(getStripeKey());
    var stripe = new StripePaymentProvider(stripeConf);
    worker.registerActivitiesImplementations(new AccountActivityImpl(stripe));
    factory.start();
  }
}
