package com.liamnbtech.server.service.connection.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.function.Supplier;

/**
 * RemoteConnectionService implementation allowing the creation of secured and unsecured connections to remote servers.
 */
@Service
public class RemoteConnectionServiceImpl implements RemoteConnectionService {

    private final SSLSocketFactory factory;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final int SOCKET_CONNECT_TIMEOUT = 10000;
    private static final int SOCKET_CONNECT_RETRIES = 50;

    @Override
    public Socket createConnection(Supplier<Socket> socketSupplier, SocketAddress endpoint)
            throws RemoteConnectionCreationException {

        Exception connectionError = null;

        for (int retry = 0; retry < SOCKET_CONNECT_RETRIES; retry++) {
            try {
                // Get an unconnected socket
                Socket toConnect = socketSupplier.get();

                // Connect the socket
                toConnect.connect(endpoint, SOCKET_CONNECT_TIMEOUT);

                // Return the connected socket
                return toConnect;
            } catch (IOException e) {
                LOG.error(String.format("Encountered error while creating remote connection: %s", e.getMessage()));
                connectionError = e;
            }
        }

        throw new RemoteConnectionCreationException(
                "Could not create connection to remote host.  Address " + endpoint,
                connectionError);
    }

    @Override
    public Socket createSSLConnection(Socket connectedSocket) throws RemoteConnectionCreationException {

        Exception socketCreationError = null;

        for (int retry = 0; retry < SOCKET_CONNECT_RETRIES; retry++) {
            try {
                return factory.createSocket(
                        connectedSocket,
                        connectedSocket.getInetAddress().getHostName(),
                        connectedSocket.getPort(),
                        true);
            } catch (IOException e) {
                socketCreationError = e;
            }
        }

        throw new RemoteConnectionCreationException(
                "Could not create SSL connection to remote host.  Address " + connectedSocket.getInetAddress(),
                socketCreationError);
    }

    public RemoteConnectionServiceImpl(SSLContext context) {
        this.factory = context.getSocketFactory();
    }
}
