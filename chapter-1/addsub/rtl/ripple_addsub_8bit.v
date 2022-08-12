`default_nettype none

module ripple_addsub_8bit(
  i_a,
  i_b,
  i_addsub,
  o_sum,
  o_overflow
);

  input   [7:0]   i_a;
  input   [7:0]   i_b;
  input           i_addsub;
  output  [7:0]   o_sum;
  output          o_overflow;

  wire    [7:0]   com_b;

  assign com_b[0] = i_b[0] ^ i_addsub;
  assign com_b[1] = i_b[1] ^ i_addsub;
  assign com_b[2] = i_b[2] ^ i_addsub;
  assign com_b[3] = i_b[3] ^ i_addsub;
  assign com_b[4] = i_b[4] ^ i_addsub;
  assign com_b[5] = i_b[5] ^ i_addsub;
  assign com_b[6] = i_b[6] ^ i_addsub;
  assign com_b[7] = i_b[7] ^ i_addsub;

  ripple_adder_8bit RA80(i_a, com_b, i_addsub, o_sum, o_overflow);

endmodule
