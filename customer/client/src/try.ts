class BeeKeeper {
    hasMask: boolean;
}

class ZooKeeper {
    nametag: string;
}

class Animal {
    numLegs: number;
}

class Bee extends Animal {
    keeper: BeeKeeper;
}

class Lion extends Animal {
    keeper: ZooKeeper;
    data: number = 1;
}

function findKeeper<A extends Animal, K>(a: {
    new (): A;
    prototype: { keeper: K, data: number };
}): K {

    return a.prototype.keeper;
}

findKeeper(Lion).nametag;  // typechecks!
