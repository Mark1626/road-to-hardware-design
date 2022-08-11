`default_nettype none

module ripple_adder_8bit(
  i_a,
  i_b,
  i_cin,
  o_sum,
  o_cout
);
  input   [7:0] i_a;
  input   [7:0] i_b;
  input         i_cin;
  output  [7:0] o_sum;
  output        o_cout;

  wire carry;

  ripple_adder_4bit RA0(i_a[3:0], i_b[3:0], i_cin, o_sum[3:0], carry);
  ripple_adder_4bit RA1(i_a[7:4], i_b[7:4], carry, o_sum[7:4], o_cout);

endmodule
