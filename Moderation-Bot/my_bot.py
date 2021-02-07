import discord
import os
from discord.ext import commands
import aiofiles
import asyncio

# Intent is used to let your bot recieve a certain information. For instance, reading a new role or working with APIs 
intents = discord.Intents.default()

# Client (bot)
client = commands.Bot(command_prefix='?', intents=intents)
intents.members = True

# It would contain guild_id = {member_id: [count, [admin_id, reason]]}
client.warnings = {}



# Sends welcome message whenever the bot is onlin
@client.event
async def on_ready():
    general_channel = client.get_channel(792765885240705095)
    await general_channel.send('Welcome, to my world!')

    for guild in client.guilds:
        async with aiofiles.open(f"{guild.id}.txt", mode="a") as temp:
            pass

        client.warnings[guild.id] = {}
    
    for guild in client.guilds:
        async with aiofiles.open(f"{guild.id}.txt", mode="r") as file:
            lines = await file.readlines()

            for line in lines:
                data = line.split(" ")
                member_id = int(data[0])
                admin_id = int(data[1])
                reason = " ".join(data[2:]).strip("\n")

                try:
                    client.warnings[guild.id][member_id][0] += 1
                    client.warnings[guild.id][member_id][1].append((admin_id, reason))
                
                except KeyError:
                    client.warnings[guild.id][member_id] = [1, [(admin_id, reason)]]

# Warns the mentioned member and stores it in a tuple (client.warnings) and increments the count of the warning the member has recieved
@client.command(name='warn')
@commands.has_permissions(administrator=True)
async def warn(context, member:discord=None, reason=None):
    if member is None or reason is None:
        warnEmbed = discord.Embed(title="Command : ?warn", color=0x00ff00)
        warnEmbed.add_field(name="Description: ", value="Warn a member", inline=False)
        warnEmbed.add_field(name="Cooldown: ", value="3 seconds", inline=False)
        warnEmbed.add_field(name="Usage: ", value="?warn [user] [reason]", inline=False)
        warnEmbed.add_field(name="Example: ", value="?warn @NoobLance Stop posting lewd images", inline=False)        

        await context.send(embed=warnEmbed)
        return
    
    try:
        isWarningFirst = False
        client.warnings[context.guild.id][member.id][0] += 1
        client.warnings[context.guild.id][member.id][1].append((context.author.id, reason))

    except KeyError:
        isWarningFirst = True
        client.warnings[context.guild.id][member.id] = [1, [(context.author.id, reason)]]

    count = client.warnings[context.guild.id][member.id][0]

    async with aiofiles.open(f"{context.guild.id}.txt", mode="a") as file:
        await file.write(f"{member.id} {context.author.id} {reason}\n")


    await context.message.channel.send(f"{member.mention} has {count} {'warning' if isWarningFirst else 'warnings'}.")

@client.command(name='warnings')
@commands.has_permissions(administrator=True)
async def warnings(context, member: discord.Member=None):
    
    if member is None:
        warningEmbed = discord.Embed(title="Command : ?warnings", color=0x00ff00)
        warningEmbed.add_field(name="Description: ", value="Get warnings for a user", inline=False)
        warningEmbed.add_field(name="Cooldown: ", value="4 seconds", inline=False)
        warningEmbed.add_field(name="Usage: ", value="?warnings [user]", inline=False)
        warningEmbed.add_field(name="Example: ", value="?warnings NoobLance", inline=False)        

        await context.send(embed=warningEmbed)
        return
    
    warningsembed = discord.Embed(title=f"Following are the warnings for {member.name}", description="", colour=discord.Colour.red())

    try:
        i = 1
        #Loops through all the tuple iterators and joins the message accordingly
        for admin_id, reason in client.warnings[context.guild.id][member.id][1]:
            admin = context.guild.get_member(admin_id)
            warningsembed.description += f"**Warning {i}** given by: {admin.mention} for: *'{reason}'*.\n"
            i += 1
        
        await context.send(embed=warningsembed)
    
    except KeyError: #no warnings
        await context.send("The member does not have any warning")


# Send goodbye message when bot is offline
@client.event
async def on_disconnect():
    general_channel = client.get_channel(792765885240705095)
    await general_channel.send('See you later, gotta help others')

@client.command(name='ban')
@commands.has_permissions(ban_members=True)
async def ban(context, member:discord.User=None, reason=None):

    if member == None and reason==None:

        banEmbed = discord.Embed(title="Command : ?ban", color=0x00ff00)
        banEmbed.add_field(name="Description: ", value="Ban a member,optional time limit", inline=False)
        banEmbed.add_field(name="Cooldown: ", value="3 seconds", inline=False)
        banEmbed.add_field(name="Sub Commands: ", value="?ban save - Ban a user and save their messages in chat.\n?ban match - Ban members who sent messages matching the text, up to 100 messages. (Must be enabled in dashboard)", inline=False)
        banEmbed.add_field(name="Usage: ", value="?ban [user] (limit) (reason)\n?ban save [user] (limit) (reason)\n?ban match [match text]", inline=False)
        banEmbed.add_field(name="Example: ", value="?ban @NoobLance Get out!\n?ban save @NoobLance Get out!\n?ban match Raided", inline=False)        

        await context.message.channel.send(embed=banEmbed)
        return

    if reason == None:  
        await member.ban()
        await context.channel.send('The user ' + member.display_name + ' has been banned.')
        return

    banMessage = f"You have been banned from {context.guild.name} for {reason}"
    await member.send(banMessage)
    await context.channel.send(f"The user {member} is banned for {reason}")
    await member.ban(reason=reason)

@client.command(name='unban')
@commands.has_permissions(ban_members=True)
async def unban(context, member:discord.User=None, reason=None):

    if member == None and reason==None:

        unbanEmbed = discord.Embed(title="Command : ?unban", color=0x00ff00)
        unbanEmbed.add_field(name="Description: ", value="Unban a member\n", inline=False)
        unbanEmbed.add_field(name="Cooldown: ", value="3 seconds\n", inline=False)
        unbanEmbed.add_field(name="Usage: ", value="?unban [user or id] [optional reason]\n", inline=False)
        unbanEmbed.add_field(name="Example: ", value="?unban NoobLance Appealed\n?unban NoobLance#0002 Really just a nice guy\n?unban 155037590859284481 Seriously, he is...", inline=False)        

        await context.message.channel.send(embed=unbanEmbed)
        print("Here first")
        return

    if reason == None:  
        await member.unban()
        await context.channel.send('The user ' + member.display_name + ' has been unbanned.')
        return

    print("Here second")
    unbanMessage = f"You have been unbanned from {context.guild.name} for {reason}"
    await member.send(unbanMessage)
    await context.channel.send(f"The user {member} is unbanned for {reason}")
    await member.unban(reason=reason)



@client.command(name='kick')
@commands.has_permissions(kick_members=True)
async def kick(context, member:discord.User=None, reason=None):

    if member == None or reason==None:

        kickEmbed = discord.Embed(title="Command : ?kick", color=0x00ff00)
        kickEmbed.add_field(name="Description: ", value="Kick a member\n", inline=False)
        kickEmbed.add_field(name="Cooldown: ", value="3 seconds\n", inline=False)
        kickEmbed.add_field(name="Usage: ", value="?kick [user or id] [reason]\n", inline=False)
        kickEmbed.add_field(name="Example: ", value="?kick @NoobLance Get out!", inline=False)        

        await context.message.channel.send(embed=kickEmbed)
        print("Here first")
        return


    print("Here second")
    kickMessage = f"You have been kicked from {context.guild.name} for {reason}"
    await member.send(kickMessage)
    await context.channel.send(f"The user {member} is kicked for {reason}")
    await member.kick()

@client.command(name='mute')
@commands.has_permissions(manage_messages=True)
async def mute(context, member:discord.Member=None, mute_time : int = 0,*,reason=None):

    guild = context.guild
    mutedRole = discord.utils.get(guild.roles, name="Muted")

    if not mutedRole:
        mutedRole = await guild.create_role(name="Muted")

        for channel in guild.channels:
            await channel.set_permissions(mutedRole, speak=False, send_messages=False, read_message_history=True, read_messages=False)

    if member == None or reason==None:

        muteEmbed = discord.Embed(title="Command : ?mute", color=0x00ff00)
        muteEmbed.add_field(name="Description: ", value="Mute a member so they cannot type or speak, time limit in minutes.\n", inline=False)
        muteEmbed.add_field(name="Cooldown: ", value="3 seconds\n", inline=False)
        muteEmbed.add_field(name="Usage: ", value="?mute [user] [limit] [reason]\n", inline=False)
        muteEmbed.add_field(name="Example: ", value="?mute @NoobLance 10 Shitposting\n?mute User 10m spamming\n?mute NoobLance 1d Too Cool", inline=False)        

        await context.message.channel.send(embed=muteEmbed)
        print("Here first")
        return


    print("Here second")
    await member.add_roles(mutedRole, reason=reason)
    muteMessage = f"You have been muted from the server {context.guild.name} for {reason}"
    await member.send(muteMessage)
    await context.channel.send(f"The user {member} is muted for {reason}")

    if mute_time > 0:
        print("Timer started")
        await asyncio.sleep(mute_time * 60)
        await member.remove_roles(mutedRole)
        unmuteMessage = f"You have been unmuted from the server {context.guild.name} as time is up"
        await member.send(unmuteMessage)
        await context.channel.send(f"The user {member} has been unmuted as time is up")
        print("Timer ended")


@client.command(name='unmute')
@commands.has_permissions(manage_messages=True)
async def unmute(context, member:discord.Member=None, reason=None):

    if member == None or reason==None:

        unmuteEmbed = discord.Embed(title="Command : ?unmute", color=0x00ff00)
        unmuteEmbed.add_field(name="Description: ", value="Unmute a member\n", inline=False)
        unmuteEmbed.add_field(name="Cooldown: ", value="3 seconds\n", inline=False)
        unmuteEmbed.add_field(name="Usage: ", value="?unmute [user] (optional reason)\n", inline=False)
        unmuteEmbed.add_field(name="Example: ", value="?unmute @NoobLance Appealed", inline=False)        

        await context.message.channel.send(embed=unmuteEmbed)
        print("Here first")
        return
    
    mutedRole = discord.utils.get(context.guild.roles, name="Muted")

    if mutedRole is None:
        await context.send(f"The member {member} is not muted in order to be unmute")
        return
    

    await member.remove_roles(mutedRole)
    await context.channel.send(f"The user {member} is unmuted for {reason}")
    unmuteMessage = f"You have been unmuted from the server {context.guild.name} for {reason}"
    await member.send(unmuteMessage)


# Clears all messages
@client.event
async def on_message(message):
    general_channel = client.get_channel(792765885240705095)
    if message.content == '$greet':
        userr_id = message.author
        await general_channel.send('{}{}{}'.format('Hello ', userr_id, ' !'))

    if message.content == '!clear':
        await general_channel.send('Clearing messages...')
        counter = 0
        async for msg in general_channel.history(): 
            await msg.delete()
            counter += 1
        await general_channel.send('{}{}{}'.format('Cleared ', counter, ' messages'))
    
    await client.process_commands(message)

# Running the client on the server
client.run('NzkyNzY0MTg1Njk5OTQyNDQw.X-idLg.OoQgvl2RhXQpahJDxZxYqT-qrqM')

