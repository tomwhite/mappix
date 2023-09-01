# Mappix

<img alt="British Isles 16x16 mappix" src="british-isles-16x16.png">

Minimal pixellated maps

## Summary

What is the smallest recognisable pixel representation of a country?

I'm not sure that there is a simple answer to this question, but I had a go at
generating some maps of the British Isles in this way.

You can see the results [here](https://tomwhite.github.io/mappix/).

## How it works

As source data I used [Land polygons including major islands](https://www.naturalearthdata.com/downloads/10m-physical-vectors/10m-land/) at 10m scale from
[Natural Earth](https://www.naturalearthdata.com/).

The program does the following:

- Load land polygons using JTS Topology Suite (from _resources/land.bin_)
- Retain only Great Britain and Ireland
- Overlay a pixel grid
- Colour a pixel black if the proportion of overlap with the polygons is greater than 50%

The code is written in Java, and outputs some Javascript code to generate a webpage.

## Improvements

Currently the grid origin is arbitrary. One improvement would be to try
different grid offsets (at multiples of e.g. 1/10 of the grid size) and see
which offset produced the "optimal" pixallation. "Optimal" could be defined in
various ways - maximum overlap, would be one, for example.
