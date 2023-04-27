# D.Java
Very easy for using Discord API and IPC library.

## Setup:
### Gradle:
```gradle
implementation files('path/to/D.Java-1.0-SNAPSHOT.jar')
```

## Custom Rich Presence using Discord IPC:
```java
DiscordIPC ipc = DiscordIPC.connectToPipe("APP_ID", new IPCEventListener() {

            // Ready event
            @Override
            public void onReady(DiscordIPC client) {
                // Set RPC Large Image and large image text
                PresenceAssets assets = new PresenceAssets()
                        .setLargeImage("win-logo")
                        .setLargeText("Build 22H2");

                // Create our activity
                Activity activity = new Activity(true)
                        .setAssets(assets) // Set large image, small image, etc...
                        .setDetails("Clean Windows 11 22H2") // Set details for activity
                        .setState("Watching YouTube") // Activity state
                        .setTimestamps(new PresenceTimestamps()) // Set start time to current
                        .setButton1(new Button("Tojex Community", "https://discord.gg/KUe5jGNbJ9")) // Add the button to activity
                        .setButton2(new Button("My Youtube", "https://www.youtube.com/channel/UCqrrQ5JzfhAIc5-Ar-7OutA")); // Add the button number 2 to activity

                client.setPresence(activity); // Update our Rich Presence
                System.out.println("Ready!");
            }
        });
```

## Authorizing in IPC:
```java
DiscordIPC ipc = DiscordIPC.connectToPipe("APP_ID", new IPCEventListener() {
            @Override
            public void onReady(DiscordIPC client) {
                client.auth("CLIENT_SECRET");
            }

            @Override
            public void onAuth(DiscordIPC client, User user, String accessToken) {
                System.out.println("Welcome, " + user.toString() + "!");
                
                // We can update Rich Presence here
            }
        });
```
