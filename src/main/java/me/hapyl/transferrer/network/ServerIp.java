package me.hapyl.transferrer.network;

import me.hapyl.transferrer.util.ServerId;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.PatternSyntaxException;

/**
 * Represents a server Ip that a player can be transferred to.
 */
public class ServerIp {
    
    private static final int defaultPort = 25565;
    
    private final ServerId serverId;
    private final InetSocketAddress address;
    
    private ServerIp(@Nonnull ServerId serverId, @Nonnull InetSocketAddress address) {
        this.serverId = serverId;
        this.address = address;
    }
    
    /**
     * Gets the {@link ServerId} of the Ip.
     *
     * @return the {@link ServerId} of the Ip.
     */
    @Nonnull
    public ServerId serverId() {
        return serverId;
    }
    
    /**
     * Gets the network address of the server.
     *
     * @return the network address of the server.
     */
    @Nonnull
    public InetSocketAddress address() {
        return address;
    }
    
    /**
     * Gets whether the network address is a localhost.
     *
     * @return whether the network address is a localhost.
     */
    public boolean isLocalhost() {
        return address.getAddress() != null && isLocalhost(address.getAddress());
    }
    
    /**
     * Gets whether this localhost network address is bound to any socket.
     *
     * @return whether this localhost network address is bound to any socket.
     * @throws IllegalStateException If called before checking whether {@link #isLocalhost()} returns {@code true}.
     */
    public boolean isLocalhostBound() throws IllegalStateException {
        if (!isLocalhost()) {
            throw new IllegalStateException("isLocalhostBound() before isLocalhost()");
        }
        
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", address.getPort()), 100);
            return true;
        }
        catch (IOException ignored) {
            return false;
        }
    }
    
    /**
     * Creates a new instance of {@link ServerIp}.
     *
     * @param serverId - The id of the server.
     * @param host     - The hostname of the server.
     * @param port     - The port of the server.
     * @return a new instance of {@link ServerIp}.
     * @throws PatternSyntaxException    if server id is malformed.
     * @throws IndexOutOfBoundsException if the port is outside the bounds.
     * @throws IllegalArgumentException  if the host is malformed.
     */
    @Nonnull
    public static ServerIp of(@Nonnull String serverId, @Nonnull String host, @Range(from = 25565, to = 65535) int port) {
        return of0(serverId, host, port);
    }
    
    /**
     * Creates a new instance of {@link ServerIp} on localhost.
     *
     * @param serverId - The id of the server.
     * @param port     - The port of the server.
     * @return a new instance of {@link ServerIp}.
     * @throws PatternSyntaxException    if server id is malformed.
     * @throws IndexOutOfBoundsException if the port is outside the bounds.
     */
    @Nonnull
    public static ServerIp of(@Nonnull String serverId, @Range(from = 25565, to = 65535) int port) {
        return of0(serverId, null, port);
    }
    
    /**
     * Creates a new instance of {@link ServerIp} on a {@link #defaultPort}.
     *
     * @param serverId - The id of the server.
     * @param host     - The hostname of the server.
     * @return a new instance of {@link ServerIp}.
     * @throws PatternSyntaxException    if server id is malformed.
     * @throws IndexOutOfBoundsException if the port is outside the bounds.
     */
    @Nonnull
    public static ServerIp of(@Nonnull String serverId, @Nonnull String host) {
        return of0(serverId, host, null);
    }
    
    /**
     * Gets whether the given port is outside the bounds.
     *
     * @param port - The port to check.
     * @return {@code true} if the given porn is outside the bounds, {@code false} otherwise.
     */
    public static boolean isPortOutsideBounds(int port) {
        return port < 25565 || port > 65535;
    }
    
    private static ServerIp of0(String serverId0, @Nullable String host0, @Nullable Integer port0) {
        final ServerId serverId = new ServerId(serverId0);
        final int port = port0 != null ? port0 : defaultPort;
        
        // Validate port
        if (isPortOutsideBounds(port)) {
            throw new IndexOutOfBoundsException("Port must be within 25565-65535!");
        }
        
        final InetSocketAddress address;
        
        // If host0 is null, use localhost
        if (host0 == null || host0.isEmpty()) {
            address = new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
        }
        else {
            address = new InetSocketAddress(host0, port);
        }
        
        return new ServerIp(serverId, address);
    }
    
    private static boolean isLocalhost(@Nonnull InetAddress address) {
        return address.isLoopbackAddress() || address.isAnyLocalAddress() || address.getHostAddress().equals("127.0.0.1") || address.getHostAddress().equals("::1");
    }
}
