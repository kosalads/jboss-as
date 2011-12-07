/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.as.controller.remote;


import org.jboss.as.protocol.mgmt.ManagementChannel;
import org.jboss.as.protocol.mgmt.ManagementMessageHandler;
import org.jboss.as.protocol.mgmt.ManagementChannelReceiver;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;
import org.jboss.remoting3.HandleableCloseable;

import java.io.IOException;

/**
 * Service used to create a new client protocol operation handler per channel
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class ModelControllerClientOperationHandlerFactoryService extends AbstractModelControllerOperationHandlerFactoryService {

    @Override
    public HandleableCloseable.Key initialize(ManagementChannel channel) {
        final ManagementMessageHandler handler = new ModelControllerClientOperationHandler(getController(), getExecutor());
        final Channel.Receiver receiver = ManagementChannelReceiver.createDelegating(handler);
        channel.setReceiver(receiver);
        return channel.addCloseHandler(new CloseHandler<Channel>() {
            @Override
            public void handleClose(Channel closed, IOException exception) {
                handler.shutdownNow();
            }
        });
    }

}
