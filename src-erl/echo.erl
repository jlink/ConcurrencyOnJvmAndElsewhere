% Adapted from "Erlang Porgramming", p. 100

-module(echo).
-export([go/0, loop/0]).

go() ->
    Pid = spawn(echo, loop, []),
    io:format("sending: hello~n", []),
    Pid ! {self(), hello},
    receive
        {Pid, Msg1} ->
            io:format("got back: ~w~n", [Msg1])
    end,
    io:format("sending: world~n", []),
    Pid ! {self(), world},
    receive
        {Pid, Msg2} ->
            io:format("got back: ~w~n", [Msg2])
    end,
    Pid ! stop.

loop() ->
    receive
        {From, Msg} ->
            From ! {self(), Msg},
            loop();
        stop ->
            true
    end.