-module(echo).
-export([go/0, loop/0]).

go() ->
    io:format("Running...", []).

loop() ->
    loop().