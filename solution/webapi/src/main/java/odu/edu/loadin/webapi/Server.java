/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package odu.edu.loadin.webapi;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.ApplicationContext;

public class Server {

    static {
        // set the configuration file
        SpringBusFactory factory = new SpringBusFactory();
        ApplicationContext context = factory.getApplicationContext();

        Bus bus = factory.createBus("ServerConfig.xml");
        BusFactory.setDefaultBus(bus);


    }

    /**
     * Creates a Server inside CXF which starts listening for requests on the URL specified.
     *
     * @throws Exception
     */
    protected Server() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();

        LoggingFeature logging = new LoggingFeature();
        logging.setPrettyLogging(true);
        sf.getFeatures().add(logging);


        //setResourceClasses() is for root resources only
        sf.setResourceClasses(
                BoxSizeServiceImpl.class,
                UserServiceImpl.class,
                InventoryServiceImpl.class,
                ExpertArticleServiceImpl.class,
                LoadPlanBoxServiceImpl.class,
                FeedbackServiceImpl.class,
                MovingTruckServiceImpl.class
        );

        sf.getInInterceptors().add(new LoadInAuthenticationInterceptor());
        sf.getInInterceptors().add(new LoadInAuthorizationInterceptor());

//        sf.setResourceProvider(BoxSizeServiceImpl.class,
//                new SingletonResourceProvider(new BoxSizeServiceImpl())
//        );
//        sf.setResourceProvider(UserServiceImpl.class,
//                new SingletonResourceProvider(new UserServiceImpl())
//        );
//        sf.setResourceProvider(InventoryServiceImpl.class,
//                new SingletonResourceProvider(new InventoryServiceImpl())
//        );
//        sf.setResourceProvider(ExpertArticleServiceImpl.class,
//                new SingletonResourceProvider(new ExpertArticleServiceImpl())
//        );
//        sf.setResourceProvider(LoadPlanBoxServiceImpl.class,
//                new SingletonResourceProvider(new LoadPlanBoxServiceImpl())
//        );
        sf.setAddress("https://localhost:9000/");

        //sf.setAddress("http://localhost:9000/");
        sf.create();
        //server.getEndpoint().

    }


    /**
     *
     * Server boot up
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        new Server();
        System.out.println("Server ready...");


        Thread.sleep(120 * 60 * 1000);  //will run for 120 minutes
        System.out.println("Server exiting");
        System.exit(0);
    }
}
