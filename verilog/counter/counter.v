`default_nettype none
`timescale 1ns/1ps

module counter(
  input clk,
  input rst,
  input en,
  output out_p
);
  reg [3:0] cnt;
  initial cnt <= 0;

  always @(posedge clk or posedge rst) begin
    if (rst) begin
      cnt <= 0;
    end
    else if (en) begin
      if (cnt == 16 - 1) begin
        cnt <= 0;
      end
      else begin
        cnt <= cnt + 1;
      end
    end
  end

  assign out_p = cnt[3];

`ifdef FORMAL
  always @(*) begin
    assert(cnt < 16);
  end

  always @(posedge clk) begin
    if ($rose(out_p))
      assert(cnt[3] == 1);
  end
`endif

endmodule
