`default_nettype none

module Add12(
    input  [31:0] x,
    output [31:0] y
);
    assign y = x + 32'h10;
endmodule

module Box(
    input         clock,
    input         reset,
    input  [31:0] x,
    output [31:0] y
);
    Add12 add(
        .x(x),
        .y(y)
    );

    // always @(posedge clock) begin
    //     y <= x + 32'b1;
    // end

endmodule
