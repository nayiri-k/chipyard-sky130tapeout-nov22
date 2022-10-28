package chipyard

import Chisel._
import chisel3._

import freechips.rocketchip.config.{Config}
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy.{AsynchronousCrossing}


import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy.{LazyModule, AddressSet}
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.rocket._
import freechips.rocketchip.tile._
import freechips.rocketchip.util._
import freechips.rocketchip.subsystem.BaseSubsystem
import freechips.rocketchip.subsystem._
import freechips.rocketchip.config.{Field, Config}
import freechips.rocketchip.tilelink.{TLRAM}
import sifive.blocks.devices.gpio._
import sifive.blocks.devices.uart._


class TiniestRocketConfig extends Config(
  new chipyard.config.WithTLSerialLocation(
    freechips.rocketchip.subsystem.FBUS,
    freechips.rocketchip.subsystem.PBUS) ++                       // attach TL serial adapter to f/p busses

  new testchipip.WithSerialTLWidth(1) ++
  new testchipip.WithDefaultSerialTL ++                           // use serialized tilelink port to external serialadapter/harnessRAM0

  new freechips.rocketchip.subsystem.WithIncoherentBusTopology ++ // use incoherent bus topology
  new chipyard.config.WithL2TLBs(0) ++                            // remove L2 TLBs
  new freechips.rocketchip.subsystem.WithNBanks(0) ++             // remove L2$
  new freechips.rocketchip.subsystem.WithNoMemPort ++             // remove backing memory
  new With1TiniestCore ++                                         // single tiny rocket-core
  new chipyard.config.AbstractConfig)


class With1TiniestCore extends Config((site, here, up) => {
  case XLen => 32
  case RocketTilesKey => List(RocketTileParams(
      core = RocketCoreParams(
        useVM = false,
        fpu = None,
        mulDiv = Some(MulDivParams(mulUnroll = 8))),
      btb = None,
      dcache = Some(DCacheParams(
        rowBits = site(SystemBusKey).beatBits,
        nSets = (16 << 10) >> 6, // 16Kb scratchpad
        nWays = 1,
        nTLBSets = 1,
        nTLBWays = 4,
        nMSHRs = 0,
        blockBytes = site(CacheBlockBytes),
        scratch = Some(0x80000000L))),
      icache = Some(ICacheParams(
        rowBits = site(SystemBusKey).beatBits,
        nSets = (4 << 10) >> 6, // 4Kb I$
        nWays = 1,
        nTLBSets = 1,
        nTLBWays = 4,
        blockBytes = site(CacheBlockBytes)))))
  case RocketCrossingKey => List(RocketCrossingParams(
    crossingType = SynchronousCrossing(),
    master = TileMasterPortParams()
  ))
})