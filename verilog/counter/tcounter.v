`timescale 1ns/1ps

module tcounter;
  reg clk;
  reg rst;
  reg en, out_reg;
  wire out;

  initial begin
    $dumpfile("test.vcd");
    $dumpvars(0, tcounter);
  end

  initial begin
    clk = 0;
    rst = 0;

    #10 rst = 1;
    #10 rst = 0;

    #500;
    $finish;
  end

  initial begin
    #20 en = 1;
    #100 en = 0;
  end

  counter c(
    .clk(clk),
    .rst(rst),
    .en(en),
    .out_p(out)
  );

  always @(posedge clk) begin
    out_reg <= out;
  end

  always #10 clk = !clk;

endmodule
