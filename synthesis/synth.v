/* Generated by Yosys 0.22 (git sha1 f109fa3d4c5, clang 14.0.0 -fPIC -Os) */

(* top =  1  *)
(* src = "../verilog/addsub/rtl/full_adder.v:3.1-24.10" *)
module full_adder(i_a, i_b, i_cin, o_sum, o_cout);
  (* hdlname = "HA0 i_a" *)
  (* src = "../verilog/addsub/rtl/full_adder.v:19.14-19.49|../verilog/addsub/rtl/half_adder.v:10.11-10.14" *)
  wire \HA0.i_a ;
  (* hdlname = "HA0 i_b" *)
  (* src = "../verilog/addsub/rtl/full_adder.v:19.14-19.49|../verilog/addsub/rtl/half_adder.v:11.11-11.14" *)
  wire \HA0.i_b ;
  (* hdlname = "HA1 i_a" *)
  (* src = "../verilog/addsub/rtl/full_adder.v:20.14-20.49|../verilog/addsub/rtl/half_adder.v:10.11-10.14" *)
  wire \HA1.i_a ;
  (* hdlname = "HA1 o_sum" *)
  (* src = "../verilog/addsub/rtl/full_adder.v:20.14-20.49|../verilog/addsub/rtl/half_adder.v:12.11-12.16" *)
  wire \HA1.o_sum ;
  (* src = "../verilog/addsub/rtl/full_adder.v:11.11-11.14" *)
  input i_a;
  wire i_a;
  (* src = "../verilog/addsub/rtl/full_adder.v:12.11-12.14" *)
  input i_b;
  wire i_b;
  (* src = "../verilog/addsub/rtl/full_adder.v:13.11-13.16" *)
  input i_cin;
  wire i_cin;
  (* src = "../verilog/addsub/rtl/full_adder.v:15.11-15.17" *)
  output o_cout;
  wire o_cout;
  (* src = "../verilog/addsub/rtl/full_adder.v:14.11-14.16" *)
  output o_sum;
  wire o_sum;
  (* module_not_derived = 32'd1 *)
  (* src = "/usr/local/bin/../share/yosys/ice40/cells_map.v:22.34-23.52" *)
  SB_LUT4 #(
    .LUT_INIT(16'hfcc0)
  ) o_cout_SB_LUT4_O (
    .I0(1'h0),
    .I1(i_a),
    .I2(i_b),
    .I3(i_cin),
    .O(o_cout)
  );
  (* module_not_derived = 32'd1 *)
  (* src = "/usr/local/bin/../share/yosys/ice40/cells_map.v:22.34-23.52" *)
  SB_LUT4 #(
    .LUT_INIT(16'hc33c)
  ) o_sum_SB_LUT4_O (
    .I0(1'h0),
    .I1(i_a),
    .I2(i_b),
    .I3(i_cin),
    .O(o_sum)
  );
  assign \HA0.i_a  = i_a;
  assign \HA0.i_b  = i_b;
  assign \HA1.i_a  = i_cin;
  assign \HA1.o_sum  = o_sum;
endmodule