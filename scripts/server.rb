require 'pry'
require 'pry-remote'
require 'redis'

require 'java'

require './jars/server/server-1.0.jar'

server = Java::OrgFirescapeServer::Server.new

class FireScape

  def world
    Java::OrgFirescapeServerModel::World.get_world
  end

  def players
    world.players
  end

  def find_player(id)
    world.players.get(id)
  end

  def give_item(player_id, item_id, quantity = 1)
    item = Java::OrgFirescapeServerModel::InvItem.new(item_id, quantity)
    i = Java::OrgFirescapeServerModel::InvItem.new(1261, 1)
    player = find_player(player_id)
    player.get_inventory.add(item)
    player.get_action_sender.send_inventory
  end
end

slack = Thread.new do
  redis = Redis.new

  redis.subscribe('game_chat') do |on|
    on.message do |channel, msg|
      # Do something with the channel messages here
    end
  end
end

mgmt = Thread.new do
  @@is_running = true

  game = FireScape.new

  def kill_serv
    @@is_running = false
    exit
  end

  while (@@is_running)
    binding.remote_pry('localhost', '9040')
  end
end

slack.join
mgmt.join

abort
