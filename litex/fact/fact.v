module Factorial(
  input         clock,
  input         reset,
  output        io_in_ready,
  input         io_in_valid,
  input  [31:0] io_in_bits,
  input         io_out_ready,
  output        io_out_valid,
  output [31:0] io_out_bits
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
`endif // RANDOMIZE_REG_INIT
  reg [31:0] n; // @[Factorial.scala 10:18]
  reg [31:0] res; // @[Factorial.scala 11:20]
  reg  state; // @[Factorial.scala 14:22]
  wire  _io_in_ready_T = ~state; // @[Factorial.scala 16:24]
  wire [63:0] _res_T = res * n; // @[Factorial.scala 26:16]
  wire [31:0] _n_T_1 = n - 32'h1; // @[Factorial.scala 27:12]
  wire  _T_7 = n == 32'h1 & state; // @[Factorial.scala 28:25]
  wire  _GEN_2 = n == 32'h1 & state ? 1'h0 : state; // @[Factorial.scala 28:45 31:11 14:22]
  wire [63:0] _GEN_3 = n > 32'h1 & state ? _res_T : {{32'd0}, res}; // @[Factorial.scala 11:20 25:43 26:9]
  wire  _GEN_6 = n > 32'h1 & state ? 1'h0 : _T_7; // @[Factorial.scala 19:16 25:43]
  wire  _GEN_7 = n > 32'h1 & state ? state : _GEN_2; // @[Factorial.scala 14:22 25:43]
  wire  _GEN_9 = _io_in_ready_T & io_in_valid | _GEN_7; // @[Factorial.scala 21:40 23:11]
  wire [63:0] _GEN_10 = _io_in_ready_T & io_in_valid ? 64'h1 : _GEN_3; // @[Factorial.scala 21:40 24:9]
  wire [63:0] _GEN_13 = reset ? 64'h1 : _GEN_10; // @[Factorial.scala 11:{20,20}]
  assign io_in_ready = ~state; // @[Factorial.scala 16:24]
  assign io_out_valid = _io_in_ready_T & io_in_valid ? 1'h0 : _GEN_6; // @[Factorial.scala 19:16 21:40]
  assign io_out_bits = res; // @[Factorial.scala 28:45 29:17]
  always @(posedge clock) begin
    if (reset) begin // @[Factorial.scala 10:18]
      n <= 32'h0; // @[Factorial.scala 10:18]
    end else if (_io_in_ready_T & io_in_valid) begin // @[Factorial.scala 21:40]
      n <= io_in_bits; // @[Factorial.scala 22:7]
    end else if (n > 32'h1 & state) begin // @[Factorial.scala 25:43]
      n <= _n_T_1; // @[Factorial.scala 27:7]
    end
    res <= _GEN_13[31:0]; // @[Factorial.scala 11:{20,20}]
    if (reset) begin // @[Factorial.scala 14:22]
      state <= 1'h0; // @[Factorial.scala 14:22]
    end else begin
      state <= _GEN_9;
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  n = _RAND_0[31:0];
  _RAND_1 = {1{`RANDOM}};
  res = _RAND_1[31:0];
  _RAND_2 = {1{`RANDOM}};
  state = _RAND_2[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule