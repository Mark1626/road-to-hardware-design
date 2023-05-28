module arbiter (
  i_clk,
  i_rst,
  i_req_0,
  i_req_1,
  o_gnt_0,
  o_gnt_1,
);

  input i_clk, i_rst, i_req_0, i_req_1;
  output o_gnt_0, o_gnt_1;

  wire i_clk, i_rst, i_req_0, i_req_1;
  reg o_gnt_0, o_gnt_1;

  always @(posedge i_clk or posedge i_rst) begin
    if (i_rst) begin
      o_gnt_0 <= 0;
      o_gnt_1 <= 0;
    end else if (i_req_0) begin
      o_gnt_0 <= 1;
      o_gnt_1 <= 0;
    end
    if (i_req_1) begin
      o_gnt_0 <= 0;
      o_gnt_1 <= 1;
    end
  end

endmodule
