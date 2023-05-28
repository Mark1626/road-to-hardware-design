`default_nettype none

module tadder;

  reg   [7:0]   a, b;
  wire  [7:0]   sum;
  wire          carry;
  reg   clk;

  initial begin
    clk = 0;
    #500;
    $finish;
  end

  ripple_adder_8bit U0(
    .i_a (a),
    .i_b (b),
    .i_cin(1'b0),
    .o_sum(sum),
    .o_cout(carry)
  );

  always @(posedge clk) begin
    a = $random;
    b = $random;
    #20;
  end

  always #10 clk = !clk;

  reg   [7:0]   a_reg, b_reg;

  always @(posedge clk) begin
    a_reg <= a;
    b_reg <= b;
  end

  always @(posedge clk) begin
    if ((a_reg > 0) && (b_reg > 0)) begin
      if (sum == a_reg + b_reg)
        $display("%d + %d = %d, Test passed", a_reg, b_reg, sum);
      else
        $display("%d + %d = %d, Test failed", a_reg, b_reg, sum);
    end
  end

endmodule

