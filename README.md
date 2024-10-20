> [!CAUTION]
> Educational purposes only.

### Current status: 🟢[Undetected](https://www.virustotal.com/gui/file/4010a8370f3473ba80ddc202cca45f5d310b344ed2e773989aff003cbea71495?nocache=1)

# KRAT

A RAT (Remote Access Trojan) written in Kotlin.

## Features
- Periodicals screenshots
- Real-time webcam
- Discord account information
- Several on-demand actions
- Everything silently and undetectable by antivirus

## Usage
1. Download the latest release and put it on a server
2. Configure the `DISCORD_BOT_TOKEN` environment variable to your bot token
3. Run the `KRAT-server.jar` file with your Discord bot token as parameter

```sh
java -jar KRAT-server.jar
```

## On-demand actions
- `/computers`: get all infected computers
- `/webcam`: take a webcam capture and send it
- `/screenshot`: take a screenshot and send it
- `/filetree`: sends the victim's file tree
- `/upload <link> <absolute-path>`: upload a file to the victim
- `/download <absolute-path>`: download a file from the victim
- `/run <file>`: execute a file on the victim's computer
- `/delete <absolute-path>`: delete a file from the victim's computer

[//]: # (3. The program will connect to your Discord bot and ask for configuration)

## Multiplatform compatibility
The project aims to be multiplatform, 
it can be compiled to run on any OS that has JVM.

| OS      | Supported |
|---------|-----------|
| Windows | ✅         |
| Linux   | ✅         |
| macOS   | ✅         |
| Android | ❌         |
| iOS     | ❌         |

## Stack
- [Kotlin](https://kotlinlang.org/)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) (asynchronous programming)
- [Ktor](https://ktor.io/) (client-server communication)
- [DiscordKt](https://github.com/DiscordKt/DiscordKt)
- [Kord](https://kordlib.github.io/kord/)
- [webcam-capture](https://github.com/sarxos/webcam-capture)