# Home Watchdog

自宅鯖の死活管理を行うためのLINE Botです。

APIサーバとしてKtorを利用しています。

## Get started

### Requirements
- Java 17 (or Higher)
- Docker

### Clone this project

```shell
$ git clone https://github.com/rokuosan/home-watchdog
```

### Launch Postgres

```shell
$ docker compose up -d
```

### Start API Server

```shell
$ ./gradlew run
```


