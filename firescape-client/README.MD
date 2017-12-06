## RSC

wut a pleb project, anyway

it's all one big intellij idea project where different game client versions are in their own submodules:

* 40
* 127
* 204
* 233

[Latest revision with a clean 204 client](https://bitbucket.org/eggsampler/rsc/src/dd5980f48766a4147abed0fb2f18f2d7f2920aa1?at=master)

out of which only 204 is really worked on, and the others are pretty much completely neglected

233 is used as a reference client in an effort of trying to make the 204 client as compatible as possible with the official game in its current state

parameters for 204's main class, mudclient, are as follows:

```
-m, --members    make the client support members servers, which all the official servers are. now that i think about it it doesn't actually do anything because it's enabled by default lol
-l, --limit30    enable limit30, whatever that is
-s, --server     server address, defaults to classic4.runescape.com
-p, --port       server port, defaults to 43594
-v, --version    game client version to use/send the server, recommended values are either 204 or 234. default: 234
    --username   username/email to login with at first
    --password   password to login with at first
-w, --width      game client width, default: 512
-h, --height     game client height, default: 334
-j, --use-jm     load certain game data from unpacked data files
```

