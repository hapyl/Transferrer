package me.hapyl.transferrer.network;

import me.hapyl.transferrer.exception.InvalidTransferPayloadException;
import me.hapyl.transferrer.util.NullableByteArray2;
import me.hapyl.transferrer.util.ObjectToBytesConverter;
import me.hapyl.transferrer.util.ServerId;
import me.hapyl.transferrer.util.Sha256;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public final class TransferPayload {
    
    private static final NamespacedKey serverFromKey = makeKey("server_from_id");
    private static final NamespacedKey serverToKey = makeKey("server_to_id");
    private static final NamespacedKey secretKey = makeKey("secret");
    
    private static final ObjectToBytesConverter<ServerId> converterServerId = ObjectToBytesConverter.of(
            id -> id.toString().getBytes(StandardCharsets.UTF_8),
            bytes -> new ServerId(new String(bytes, StandardCharsets.UTF_8))
    );
    private static final ObjectToBytesConverter<Sha256> converterSecret = ObjectToBytesConverter.of(Sha256::encoded, bytes -> Sha256.fromString(new String(bytes, StandardCharsets.UTF_8)));
    
    private final ServerId serverFromId;
    private final ServerId serverToId;
    private final Sha256 secret;
    
    TransferPayload(@Nonnull ServerId serverFromId, @Nonnull ServerId serverToId, @Nonnull Sha256 secret) {
        this.serverFromId = serverFromId;
        this.serverToId = serverToId;
        this.secret = secret;
    }
    
    @Nonnull
    public ServerId serverFromId() {
        return serverFromId;
    }
    
    @Nonnull
    public ServerId serverToId() {
        return serverToId;
    }
    
    @Nonnull
    public Sha256 secret() {
        return secret;
    }
    
    public void store(@Nonnull Player player) {
        player.storeCookie(serverFromKey, converterServerId.asBytes(serverFromId));
        player.storeCookie(serverToKey, converterServerId.asBytes(serverToId));
        player.storeCookie(secretKey, converterSecret.asBytes(secret));
    }
    
    @Nonnull
    public static CompletableFuture<@Nullable TransferPayload> retrieve(@Nonnull Player player) {
        final CompletableFuture<byte @Nullable []> serverFromFuture = player.retrieveCookie(serverFromKey);
        final CompletableFuture<byte @Nullable []> serverToIdFuture = player.retrieveCookie(serverToKey);
        final CompletableFuture<byte @Nullable []> secretFuture = player.retrieveCookie(secretKey);
        
        return serverFromFuture
                .thenCombine(serverToIdFuture, NullableByteArray2::of)
                .thenCombine(
                        secretFuture, (array, secretCookie) -> {
                            final byte[] serverFromCookie = array.byte1();
                            final byte[] serverToCookie = array.byte2();
                            
                            if (serverFromCookie == null || serverFromCookie.length == 0) {
                                throw new InvalidTransferPayloadException(InvalidTransferPayloadException.Cause.ILLEGAL_SERVER_FROM);
                            }
                            
                            if (serverToCookie == null || serverToCookie.length == 0) {
                                throw new InvalidTransferPayloadException(InvalidTransferPayloadException.Cause.ILLEGAL_SERVER_TO);
                            }
                            
                            if (secretCookie == null || secretCookie.length == 0) {
                                throw new InvalidTransferPayloadException(InvalidTransferPayloadException.Cause.MALFORMED_SECRET);
                            }
                            
                            final ServerId serverFromId = converterServerId.asObject(serverFromCookie, InvalidTransferPayloadException.Cause.MALFORMED_SERVER_FROM);
                            final ServerId serverToId = converterServerId.asObject(serverToCookie, InvalidTransferPayloadException.Cause.MALFORMED_SERVER_TO);
                            final Sha256 secret = converterSecret.asObject(secretCookie, InvalidTransferPayloadException.Cause.MALFORMED_SECRET);
                            
                            return new TransferPayload(serverFromId, serverToId, secret);
                        }
                );
    }
    
    private static NamespacedKey makeKey(String key) {
        // Using string namespace instead of plugin to not expose the class
        return new NamespacedKey("transferrer", key);
    }
    
}
