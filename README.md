# Transferrer
An secure transfer API for Paper.

## About
Allows for secure transferring between Minecraft servers with the API.

> [!WARNING]
> Installing this plugin will block any transfers not made through the API, including usage of Player#transfer() and the /transfer command.


## Installation
1. Add a maven dependency:
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.hapyl</groupId>
    <artifactId>Transferrer</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```
[![](https://jitpack.io/v/hapyl/Transferrer.svg)](https://jitpack.io/#hapyl/Transferrer)

2. Download the [latest](https://github.com/hapyl/Transferrer/releases/latest) release and put it in all of your servers' /plugins folder.
3. Set `accepts-transfers` to `true` in your `server.properties`.
4. Modify the `config.yml` on each server, changing:
    - `server_id` to a unique identifier of your server, following the `^[a-z0-9_]+$` pattern.
    - `secret` to a universal sha256 string; *all servers **must** have the same secret.*
    - `accepts` to a list of server ids that are allowed to transfer to this sever, or `"*"` ***(Must be in quotations because of serialization!)*** wildcard to allow all servers.

## Usage Example
After installation is complete, you can finally transfer players, by either using the built-in API or running a command.
*The command is designed to mainly be used as a 'debug' and transferring should only be done using the API.*

```java
final ServerIp development = ServerIp.of("development", 33333);
        
Transferrer.getAPI().transfer(player, development);
```
> Attempts to transfer the player to `development` server on `localhost:33333`.

<br>

```minecraft
/transferrer goto development localhost 33333
```
> The equivalent using the command. *(Not recommended)*
