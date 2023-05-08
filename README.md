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

### Write .env file

```shell
$ nano .env
```

Set your API tokens.

```properties
CHANNEL_SECRET=<YOUR_CHANNEL_SECRET>
CHANNEL_ACCESS_TOKEN=<YOUR_ACCESS_TOKEN>
```

### Create venv (Python)

Make a new environment.

```shell
# Windows
$ python -m venv venv
# Others
$ python3 -m venv venv
```

Activate.

```shell
$ source ./venv/bin/activate
# or
$ ./venv/Scripts/Activate.ps1
```

Install dependencies.

```shell
$ pip install -r requirements.txt
```

### Launch Postgres

```shell
$ docker compose up -d
```

### Start API Server

```shell
$ ./gradlew run
```


