- module(storehouse) .
- export([ new_shelf / 0 , put_in / 2 ]) .
- export([ start / 0 , stop / 0 , init / 0 ]) .

new_shelf () ->[ ] .

put_in (Shelf , Product) ->
[ Product | Shelf ] .

start() ->
    .

init() ->
    .

stop() ->
    .

loop(Shelves) ->
    receive
