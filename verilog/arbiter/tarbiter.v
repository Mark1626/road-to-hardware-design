module tarbiter;

  reg clock, reset, req0, req1;
  wire gnt0, gnt1;

  initial begin
    $dumpfile("test.vcd");
    $dumpvars(0,tarbiter);
    $monitor ("At time %t req0=%b,req1=%b,gnt0=%b,gnt1=%b", $time, req0, req1, gnt0, gnt1);
    clock = 0;
    reset = 0;
    req0  = 0;
    req1  = 0;

    #5 reset          = 1;
    #15 reset         = 0;
    #10 req0          = 1;
    #10 req0          = 0;
    #10 req1          = 1;
    #10 req1          = 0;
    #10 {req0, req1}  = 2'b11;
    #10 {req0, req0}  = 2'b00;
    #10 $finish;
  end

  always begin
    #5 clock = !clock;
  end

  arbiter U0(
    .i_clk (clock),
    .i_rst (reset),
    .i_req_0 (req0),
    .i_req_1 (req1),
    .o_gnt_0 (gnt0),
    .o_gnt_1 (gnt1)
  );

endmodule
