package examples.dataflow

import groovyx.gpars.dataflow.DataflowQueue
import static groovyx.gpars.dataflow.Dataflow.*

def leftAddend = new DataflowQueue()
def rightAddend = new DataflowQueue()
def sum = new DataflowQueue()

operator( inputs: [leftAddend, rightAddend],
          outputs: [sum],
          { chance, amount -> sum << chance + amount } )

task {
    [10, 20, 30].each { leftAddend << it }
}
task {
    [100, 200, 300].each { rightAddend << it }
}

[110, 220, 330].each { assert it == sum.val }