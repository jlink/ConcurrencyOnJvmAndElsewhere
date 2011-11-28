- module(storehouse) .
- export([ new_shelf / 0 , put_in / 2 ]) .
- export([ start / 0 , stop / 0 , init / 0 ]) .
- export([ call / 1 ]) .

new_shelf () ->[ ] .

put_in (Shelf , Product) ->
    [ Product | Shelf ] .

start () ->
    register(storehouse, spawn(storehouse, init, [])).

init() ->
    loop([]).

stop() ->
    call(stop).

loop(Shelves) ->
    receive
        {request, From, Msg} when(Msg /= stop) ->
            io:format("received: ~w~n", [Msg]),
            reply(From, ok),
            loop(Shelves);
        {request, From, stop} ->
            reply(From, ok)
    end.

call(Message) ->
    storehouse ! {request, self(), Message},
    receive
        {reply, Reply} -> Reply
    end.

reply(Pid, Reply) ->
    Pid ! {reply, Reply}.