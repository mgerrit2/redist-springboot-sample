## Redis template

## key conventions
app:object_type:id:attribute

### samples
demoredis:testkey:10:test

## lettuce

<div>
    <a href="https://www.youtube.com/watch?v=H_DFM_CCEGI">
    Episode 17: Spring boot and redis integration using lettuce
    </a>
</div>

<div>
    <a href="https://github.com/redis/lettuce?tab=readme-ov-file">
    source code
    </a>
</div>


## Acces webservice in vmware from outside

### set the VM vmware
> virtual machine settings->NAT->connected->true
> 
> virtual machine settings->NAT->connected at power on->
> 
> virtual machine settings->NAT->NAT: Used to share the host's IP adress->true

### generete the ip adress
#### you must set this up to get the IP adress
##### Bring the interface down
> sudo ip link set ens33 down

##### Bring the interface back up and request a DHCP lease
>sudo dhclient ens33


#### list the ip adress, and get this for accessing the vm
ip addr show ens33
