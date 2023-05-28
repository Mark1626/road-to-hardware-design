`default_nettype none

module blinker #(
  parameter                 WIDTH = 32,
  parameter                 CLOCK_RATE_HZ = 100_000_000,
  parameter [(WIDTH-1):0]   INCREMENT = (1<<(WIDTH-2)) / (CLOCK_RATE_HZ/4)
) (
  input     wire              i_clk, i_rst,
  output    wire              o_out
);

  reg [(WIDTH-1) : 0] counter;

  always @(posedge i_clk or posedge i_rst)
    if (i_rst)
      counter <= 0;
    else
      counter <= counter + INCREMENT;

  assign o_out = counter[31];

endmodule
