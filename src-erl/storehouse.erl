- module(storehouse) .
- export([ new_shelf / 0 , put_in / 2 ]) .
- export([ start / 0 , stop / 0 , init / 0 ]) .
- export([ call / 1 , create_shelf / 1]) .

new_shelf () ->[ ] .

put_in (Shelf , Product) ->
    [ Product | Shelf ] .

start () ->
    register(storehouse, spawn(storehouse, init, [])).

init() ->
    loop([]).

stop() ->
    call(stop).

create_shelf(Shelves, Name) ->
    Shelf = new_shelf(),
    {Shelf, [{Name, Shelf}|Shelves]}.


loop(Shelves) ->
    receive
        {request, From, {create_shelf, Name}} ->
            {Shelf, NewShelves} = create_shelf(Shelves, Name),
            reply(From, Shelf),
            loop(NewShelves);
        {request, From, stop} ->
            reply(From, ok);
        {request, From, Msg} ->
            io:format("received: ~w~n", [Msg]),
            reply(From, ok),
            loop(Shelves)
    end.

call(Message) ->
    storehouse ! {request, self(), Message},
    receive
        {reply, Reply} -> Reply
    end.

create_shelf(Name) ->
    call({create_shelf, Name}).

reply(Pid, Reply) ->
    Pid ! {reply, Reply}.