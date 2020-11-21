package com.liamnbtech.server.service.connection.remote;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.function.Supplier;

/**
 * Interface for a service allowing the creation of secured and unsecured connections to remote servers.
 */
public interface RemoteConnectionService {

    /**
     * Establishes and returns a connection to the remote entity specified by the provided address.
     *
     * @param endpoint Address of the entity to which a connection should be established
     * @param socketSupplier A function producing the unconnected socket to connect to the specified address
     *
     * @return Socket representing the connection to the remote entity
     *
     * @throws RemoteConnectionCreationException If the connection could not be established for any reason
     */
    Socket createConnection(Supplier<Socket> socketSupplier, SocketAddress endpoint) throws RemoteConnectionCreationException;

    /**
     * Establishes and returns a secure connection to the remote host connected to the given socket.
     *
     * @param connectedSocket The insecurely connected socket
     *
     * @return Socket representing the secure connection to the remote host connected to the given socket
     *
     * @throws RemoteConnectionCreationException If the secure connection could not be established for any reason
     */
    Socket createSSLConnection(Socket connectedSocket) throws  RemoteConnectionCreationException;
}
