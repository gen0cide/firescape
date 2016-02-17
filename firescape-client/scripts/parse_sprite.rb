require 'pry'
require 'json'

SPRITE_DIR = './packed-sprites'
OUTPUT_DIR = './media/sprites'

sprites = {}

def get_file_array
  Dir[File.join(SPRITE_DIR, '*')].map{|i| Integer(i.split('/')[-1])}.sort
end

def open_sprite(id)
  File.open(File.join(SPRITE_DIR, id.to_s))
end

def read_int(f)
  f.read(4).unpack('N')[0]
end

def read_char(f)
  val = f.read(1).unpack('C')[0]
  if val == 1
    return true
  else
    return false
  end
end

ids = get_file_array

ids.each do |id|
  f = open_sprite(id)
  width = read_int(f)
  height = read_int(f)
  require_shift = read_char(f)
  x_shift = read_int(f)
  y_shift = read_int(f)
  meta1 = read_int(f)
  meta2 = read_int(f)

  sprites[id] = {
    width: width,
    height: height,
    require_shift: require_shift,
    x_shift: x_shift,
    y_shift: y_shift,
    something1: meta1,
    something2: meta2
  }

  puts "[*] Parsed Sprite #{id.to_s}"
  f.close
end

File.open(File.join(OUTPUT_DIR, 'manifest.json'),'w') do |f|
  f.puts sprites.to_json
end

