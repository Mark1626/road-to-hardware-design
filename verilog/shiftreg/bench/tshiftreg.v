module tshiftreg;
  reg         clk;
  reg         rst;
  reg         inp;
  wire [7:0]  out;

  initial begin
    $dumpfile("tshiftreg.vcd");
    $dumpvars(0, tshiftreg);
    clk = 0;
    rst = 1;
    #20;
    rst = 0;
    #250;
    rst = 1;
    #10;
    rst = 0;
    #500;
    $finish;
  end

  shiftreg R0(
    .i_clk (clk),
    .i_rst (rst),
    .i_input (inp),
    .o_out (out)
  );

  always @(posedge clk) begin
    inp <= $random;
    #20;
  end

  always #10 clk = !clk;

  always @(posedge clk) begin
    $display("rst: %b inp: %b reg: %b", rst, inp, out);
  end

endmodule
