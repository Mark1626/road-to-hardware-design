`default_nettype none

module full_adder(
  i_a,
  i_b,
  i_cin,
  o_sum,
  o_cout
);

  input i_a;
  input i_b;
  input i_cin;
  output o_sum;
  output o_cout;

  wire sum_1, carry_1, carry_2;
  half_adder HA0(i_a,     i_b, sum_1, carry_1);
  half_adder HA1(i_cin, sum_1, o_sum, carry_2);

  assign o_cout = carry_1 | carry_2;

endmodule
