`default_nettype none

module shiftreg #(
  parameter     WIDTH = 8
) (
  input   wire                i_clk,
  input   wire                i_rst,
  input   wire                i_input,
  output  reg  [(WIDTH-1):0]  o_out
);
  initial o_out     = 0;

  always @(posedge i_clk or posedge i_rst) begin
    if (i_rst)
      o_out <= 0;
    else
      o_out <= { o_out[(WIDTH-2):0], i_input };
  end

endmodule
