- module(keyvalue) .
- export([ go / 0, init / 0 ]) .

go() ->
    Store = spawn(keyvalue, init, []),
    Store ! {key, self(), "name"},
    Store ! {value, self(), "Johannes"},
    Store ! {get, self(), "name"},
    receive
        {Store, Value1} -> io:format("Value of 'name': ~w~n", [Value1])
    end,
    Store ! {get, self(), "age"},
    receive
        {Store, Value2} -> io:format("Value of 'age': ~w~n", [Value2])
    end,
    Store ! stop.

init() ->
    loop([]).

loop(KeyValues) ->
    receive
        {key, Sender, Key} ->
            receive
                {value, Sender, Value} ->
                    NewElement = {Key, Value},
                    loop([NewElement | KeyValues])
            end;
        {get, Sender, Key} ->
            reply(Sender, getValue(KeyValues, Key)),
            loop(KeyValues);
        stop -> ok
    end.

getValue([], _) -> nothing;

getValue([KeyValue|Rest], Key) ->
    case KeyValue of
        {Key, Value} -> Value;
        _ -> getValue(Rest, Key)
    end.

reply(Sender, ReplyMsg) ->
    Sender ! {self(), ReplyMsg}.