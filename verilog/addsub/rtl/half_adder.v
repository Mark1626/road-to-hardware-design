`default_nettype none

module half_adder(
  i_a,
  i_b,
  o_sum,
  o_cout
);

  input   i_a;
  input   i_b;
  output  o_sum;
  output  o_cout;

  assign o_sum  = i_a ^ i_b;
  assign o_cout = i_a & i_b;

endmodule
