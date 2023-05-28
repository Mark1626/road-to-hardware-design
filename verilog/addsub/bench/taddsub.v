`default_nettype none

module tsubtracter;

  reg   [7:0]   a, b;
  wire  [7:0]   sum;
  reg           addsub;
  wire          overflow;
  reg           clk;

  initial begin
    $dumpfile("test.vcd");
    $dumpvars(0, tsubtracter);
    #25
    clk = 0;
    addsub = 1;
    #100;
    addsub = 0;
    #100;
    $finish;
  end

  ripple_addsub_8bit U0(a, b, addsub, sum, overflow);

  always @(posedge clk) begin
    a = $random;
    b = $random;
    #10;
  end

  reg   [7:0]   a_reg, b_reg, addsub_reg;

  always @(posedge clk) begin
    a_reg <= a;
    b_reg <= b;
    addsub_reg <= addsub;
  end

  always #5 clk = !clk;

  always @(posedge clk) begin
    if ((a_reg > 0) && (b_reg > 0)) begin
      
      if (addsub_reg == 0) begin
        if (sum == a_reg + b_reg)
          $display("%d + %d = %d, Test passed", a_reg, b_reg, sum);
        else
          $display("%d + %d = %d, Test failed", a_reg, b_reg, sum);
      end

      if (addsub_reg == 1) begin
        if (sum == a_reg - b_reg)
          $display("%d - %d = %d, Test passed", a_reg, b_reg, sum);
        else
          $display("%d - %d = %d, Test failed", a_reg, b_reg, sum);
      end
    end
  end

endmodule
