package examples.dataflow

import groovyx.gpars.dataflow.DataflowVariable
import static groovyx.gpars.dataflow.Dataflow.*

final dichte = new DataflowVariable()
final gewicht = new DataflowVariable()
final volumen = new DataflowVariable()
task { dichte << gewicht.val / volumen.val }
task { gewicht << 10.6 }
task { volumen << 5.0 }
assert dichte.val == 2.12