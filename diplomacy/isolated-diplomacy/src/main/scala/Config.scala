import org.chipsalliance.cde.config.Config

class AdderConfig extends Config((site, here, up) => {
  case NumOperands => 4
  case BitWidth => 8
})
