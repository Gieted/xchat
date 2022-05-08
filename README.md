# xchat

-----------

Xchat is a CLI chat application based on REST.

## Installation

-----------
Pre-requirements:

- Java 17 installed
- `JAVA_HOME` set to the location of your Java installation

The installation procedure:

1. Download the latest release from [here](https://github.com/pawelkielb/fchat/releases/download/1.0.0/client.zip).
2. Unpack it to any folder you wish.
3. Add `bin/` folder to your `PATH`.
4. Restart the computer or restart explorer in the task manager.
5. Open a terminal and try using `xchat` command. If the installation was successful you should see a command list.

## Setup

-----------

Create a new directory and open a new terminal window there.

```bash
mkdir xchat 
cd xchat
```

Run `xchat init` command. This should create an `xchat.properties` file inside the folder.

```properties
#Sun May 08 10:41:13 CEST 2022
server_host=http://localhost
server_port=8080
username=Guest
```

Now edit the configuration to your preferences. Save the file, your client is now ready to use. You can have multiple
clients in a separate directories.

## Commands

-----------

### xchat init

Creates a xchat.properties file in the current directory.

```
xchat init
```

### xchat create

Creates a new group channel. You can use `cd` command to enter the channel. Other users must use `xchat sync` to see the
channel.

```
xchat create [name] (usernames...)

e.g. xchat create "Small Talks" ben steven
```

- name - a name of the channel
- usernames - 0 or more usernames of users who should be added to the channel

### xchat priv

Creates a new private channel. You can use `cd` command to enter the channel. The other user must use `xchat sync` to
see the channel.

```
xchat priv [username]

e.g. xchat priv christine
```

- username - a username of a user you want to chat with

### xchat sync

Updates the channels list.

```
xchat sync
```

### xchat send

Sends a message to a channel. To use the command you have to first `cd` to a channel directory.

```
xchat send [message...]

e.g. xchat send Hello Threre!
```

### xchat read

Reads a messages from a channel. To use the command you have to first `cd` to a channel directory.

```
xchat read (count)
```

- a count of last messages to read

### xchat sendfile

Sends a file to a channel. To use the command you have to first `cd` to a channel directory.

```
xchat sendfile [path]
```

- path - a path of a file to send

### xchat download

Downloads a file from a channel. The file will be saved in the current directory. To use the command you have to
first `cd` to a channel directory.

```
xchat download [name]
```

- a name of a file to download

## Running the server locally

-----------

In order to run the server locally:

1. Clone the repository
2. Start docker using `docker-compose up`
3. Run `./gradlew server:run`
4. A default server port is 8080. It can be changed using a `PORT` environmental variable
