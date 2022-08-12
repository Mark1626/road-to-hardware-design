`default_nettype none

module ripple_adder_4bit(
  i_a,
  i_b,
  i_cin,
  o_sum,
  o_cout
);
  input   [3:0] i_a;
  input   [3:0] i_b;
  input         i_cin;
  output  [3:0] o_sum;
  output        o_cout;

  wire carry_1, carry_2, carry_3;

  full_adder FA0(i_a[0], i_b[0], i_cin, o_sum[0], carry_1);
  full_adder FA1(i_a[1], i_b[1], carry_1, o_sum[1], carry_2);
  full_adder FA2(i_a[2], i_b[2], carry_2, o_sum[2], carry_3);
  full_adder FA3(i_a[3], i_b[3], carry_3, o_sum[3], o_cout);

endmodule
