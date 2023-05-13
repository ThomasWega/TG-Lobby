# TGLobby
Lobby for TrustGames.net

###### Configs:
- Join/Leave messages
- Double Jump
- PiggyBack
- Build Command messages
- Spawn Command messages

###### Features:
- Hotbar Items
- Join/Leave messages send
- Double Jump
- PiggyBack
- Build/Place protection
- Spawn teleport
- XP Bar show level + update

###### Commands:
- /build <name>
- /setspawn
- /spawn <name>


###### How to get Core:
TG-Core is self-hosted on a server. To be able to reach that server you need to set the server up in your maven settings.xml. Insert the following lines in the server section

**_settings.xml_**
```
<servers>
    <server>
      <id>trustgames-repo</id>
      <username>{username}</username>
      <password>{secret}</password>
    </server>
  </servers>
```

