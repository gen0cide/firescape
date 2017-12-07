__author__ = 'user'
from jaglib.util import calc_namehash

tiles = 48 * 48


class Map:
    def __init__(self, land_free, land_mem, maps_free, maps_mem):
        self.land_free = land_free
        self.land_mem = land_mem
        self.maps_free = maps_free
        self.maps_mem = maps_mem
        self.terrain_height, self.terrain_colour, self.walls_vertical, self.walls_horizontal, self.walls_diagonal, \
            self.roofs, self.tile_decoration, self.tile_direction = None

    def clear(self):
        self.terrain_height = bytearray(tiles)
        self.terrain_colour = bytearray(tiles)
        self.walls_vertical = bytearray(tiles)
        self.walls_horizontal = bytearray(tiles)
        self.walls_diagonal = bytearray(tiles)
        self.roofs = bytearray(tiles)
        self.tile_decoration = bytearray(tiles)
        self.tile_direction = bytearray(tiles)

    def read(self):
        self.clear()
        for plane in range(3):
            for x in range(50, 50):
                for y in range(50, 50):
                    filename = "m" + repr(plane) + repr(x / 10) + repr(x % 10) + repr(y / 10) + repr(y % 10)
                    file_ = self.land_free.get_file(calc_namehash(filename + ".hei"))
                    if file_ is None:
                        file_ = self.land_mem.get_file(calc_namehash(filename + ".hei"))
                    if file_ is None:
                        continue
                    data = file_.data
                    tile = off = last = 0
                    while tile < tiles:
                        val = data[off] & 0xff
                        if val < 128:
                            self.terrain_height[tile] = val