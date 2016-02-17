require 'pry'
require 'pry-remote'

require 'java'

require './lib/hex-string.jar'
require './lib/jmf.jar'
require './lib/junit.jar'
require './lib/mina.jar'
require './lib/slf4j.jar'
require './lib/xpp3.jar'
require './lib/xstream.jar'
require './lib/commons-io.jar'
require './lib/gson.jar'

require './rscd.jar'

client = Java::OrgRscdaemonClient::mudclient

client.start_script

mc = client.getmc

mgmt = Thread.new do
  @@is_running = true

  # game = FireScape.new

  def kill_serv
    @@is_running = false
    exit
  end

  while(@@is_running)
    binding.remote_pry('localhost', '9041')
  end
end

# slack.join
mgmt.join

abort
